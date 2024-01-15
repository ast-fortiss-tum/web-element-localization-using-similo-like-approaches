#!/bin/bash

# Get OS and architecture
os=$(uname -s)
arch=$(uname -m)

# Set the base URL for GeckoDriver downloads
base_url="https://github.com/mozilla/geckodriver/releases/download/v0.33.0"

# Function to download and unzip GeckoDriver
download_geckodriver () {
  wget "${base_url}/geckodriver-${1}.tar.gz"
  tar -xvzf "geckodriver-${1}.tar.gz"
  rm -f "geckodriver-${1}.tar.gz"
  chmod +x geckodriver
}

# Check if GeckoDriver already exists
if [ -f "geckodriver" ]; then
  read -p "GeckoDriver already exists. Do you want to remove it and download a new one? (y/n) " -n 1 -r
  echo
  if [[ $REPLY =~ ^[Yy]$ ]]; then
    rm -f geckodriver
  else
    echo "GeckoDriver not downloaded."
    exit 0
  fi
fi

# Check the OS and architecture and download the correct GeckoDriver
if [ "$os" == "Linux" ]; then
  if [ "$arch" == "x86_64" ]; then
    download_geckodriver "v0.33.0-linux64"
  else
    download_geckodriver "v0.33.0-linux-aarch64"
  fi
elif [ "$os" == "Darwin" ]; then
  if [ "$arch" == "arm64" ]; then
    download_geckodriver "v0.33.0-macos-aarch64"
  else
    download_geckodriver "v0.33.0-macos"
  fi
else
  echo "Unsupported OS."
  exit 1
fi

echo "GeckoDriver downloaded."
