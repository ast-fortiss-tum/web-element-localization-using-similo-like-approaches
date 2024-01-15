package optimization

import evalutation.EvaluationData.SiteData
import evalutation.{EvaluationComputing, EvaluationStatistics}
import util.Utils.{asJava, roundTwoDecimals}
import util.{Load, Utils}
import io.jenetics.*
import io.jenetics.engine.{Engine, EvolutionResult}
import optimization.GeneticOptimization.{JavaDouble, JavaFunction, getDoubles}
import similo.comparing.LegacyCompare.*
import similo.comparing.*
import similo.similo.Similo
import similo.von.VonSimilo
import similo.{AttributeComparison, Locator}

import java.time.LocalDateTime

case class GeneticOptimization[G <: NumericGene[_, G], L <: Locator](
  locator: Seq[Double] => L,
  fitnessBy: (Seq[Double] => L) => JavaFunction[Genotype[G], JavaDouble],
  genotype: Int => Genotype[G],
  props: Int = 16,
  outfile: String = "results/weight_opt/" + LocalDateTime.now() + ".csv"
):

  def compute(): Unit =
    Utils.write(outfile, s"generation,best_fitness,${1 to props mkString ","}")
    val engine =
      Engine.builder(fitnessBy(locator), genotype(props)).populationSize(100).optimize(Optimize.MAXIMUM).build
    val result = engine.stream.limit(100).peek(printEvaluationResult).collect(EvolutionResult.toBestGenotype)
    val similo = locator(getDoubles(result))
    println(s"Optimal locator is: $similo")

  private def printEvaluationResult(result: EvolutionResult[G, ?]): Unit =
    println(s"Generation: ${result.generation()}")
    println(s"Best fitness: ${result.bestFitness()}")
    println(s"Best phenotype: ${result.bestPhenotype()}")
    Utils.append(outfile, s"${result.generation()},${result.bestFitness()},${result.bestPhenotype()}")

object GeneticOptimization:

  private type JavaInt = java.lang.Integer
  private type JavaDouble = java.lang.Double
  private type JavaFunction[I, O] = java.util.function.Function[I, O]
  private type FitnessFunction[Gen <: NumericGene[_, Gen]] = JavaFunction[Genotype[Gen], JavaDouble]
  private type FitnessFor[Gen <: NumericGene[_, Gen], L <: Locator] = (Seq[Double] => L) => FitnessFunction[Gen]

  def fitnessByScore[G <: NumericGene[_, G], L <: Locator]: FitnessFor[G, L] = fitnessBy(_.score)(identity)

  def fitnessByScoreFirstLast[G <: NumericGene[_, G], L <: Locator]: FitnessFor[G, L] =
    fitnessBy(_.score)(EvaluationComputing.firstAndLastMapper)
  
  private def fitnessBy[G <: NumericGene[_, G], L <: Locator](metric: EvaluationStatistics => Double)(mapper: SiteData => SiteData = identity)(
    locator: Seq[Double] => L
  ): FitnessFunction[G] =
    asJava: genotype =>
      val values = getDoubles(genotype)
      val similo = locator(values)
      val stats = EvaluationComputing.computeAll(similo, mapper = mapper)
      val score = metric(stats)
      println(s"Score: $score for $similo")
      score

  private def getDoubles[G <: NumericGene[_, G]](gt: Genotype[G]): Seq[Double] =
    (0 until gt.length).map: i =>
      gt.get(i).gene() match
        case d: DoubleGene  => d.doubleValue()
        case i: IntegerGene => roundTwoDecimals(i.intValue() * 0.05)
        case _              => throw new IllegalArgumentException("Only DoubleGene and IntegerGene are supported")

  def fitnessByTopTen[G <: NumericGene[_, G], L <: Locator]: FitnessFor[G, L] = fitnessBy(_.matchInFound)(identity)

  def inputSimilo(values: Seq[Double]): Similo = inputSimiloAlgorithm(values, Similo.apply)

  def inputVonSimilo(values: Seq[Double]): VonSimilo =
    inputSimiloAlgorithm(values, VonSimilo.apply(_))

  private def inputSimiloAlgorithm[L <: Locator](values: Seq[Double], input: Seq[AttributeComparison[_]] => L): L =
    input(
      Seq(
        AttributeComparison(_.tag, values.head, StringCompare.jaroWinkler),
        AttributeComparison(_.className, values(1), StringCompare.equal.forOption),
        AttributeComparison(_.name, values(2), StringCompare.equal.forOption),
        AttributeComparison(_.id, values(3), StringCompare.equal.forOption),
        AttributeComparison(_.href, values(4), StringCompare.equal.forOption),
        AttributeComparison(_.alt, values(5), StringCompare.equal.forOption),
        AttributeComparison(_.typeName, values(6), StringCompare.equal.forOption),
        AttributeComparison(_.ariaLabel, values(7), StringCompare.jaccard.forOption),
        AttributeComparison(_.xpath.chrome, values(8), StringCompare.equal),
        AttributeComparison(_.xpath.idxpath, values(9), StringCompare.levenshtein),
        AttributeComparison(_.isButton, values(10), ValueCompare.equals),
        AttributeComparison(_.location, values(11), LocationCompare.distanceSmallDecay),
        AttributeComparison(_.dimension, values(12), DimensionCompare.area),
        AttributeComparison(_.getVisibleText, values(13), StringCompare.levenshtein.forOption),
        AttributeComparison(_.neighbourText, values(14), StringCompare.levenshtein.forOption),
        AttributeComparison(_.attributes, values(15), MapCompare.intersectValueCompare)
      )
    )

  def createDoubleGenotype(n: Int): Genotype[DoubleGene] =
    createGenotype(n, DoubleChromosome.of(0.0, 3.0, 100))

  def createIntGenotype(n: Int): Genotype[IntegerGene] =
    createGenotype(n, IntegerChromosome.of(0, 60))

  private def createGenotype[G <: Gene[_, G]](n: Int, generator: => AbstractChromosome[G]): Genotype[G] =
    val genes = (0 until n).map(_ => generator)
    Genotype.of(genes.head, genes.tail*)
