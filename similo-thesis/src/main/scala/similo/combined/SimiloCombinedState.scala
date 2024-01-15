package similo.combined

import similo.State
import similo.similo.SimiloState
import similo.von.VonSimiloState

case class SimiloCombinedState(similoState: SimiloState, vonSimiloState: VonSimiloState) extends State
