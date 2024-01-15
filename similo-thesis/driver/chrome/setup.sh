#!/bin/bash

# Get OS and architecture
os=$(uname -s)
arch=$(uname -m)

# Determine the latest version of ChromeDriver for Chrome version 116
latest_version=$(curl -s "https://googlechromelabs.github.io/chrome-for-testing/LATEST_RELEASE_120")

# Function to download and unzip ChromeDriver
download_chromedriver () {
  local url=$1
  wget "$url"
  local filename=$(basename "$url")
  unzip "$filename" -d .
  rm -f "$filename"
  chmod +x chromedriver
}

# Check if ChromeDriver already exists
if [ -f "chromedriver" ]; then
  read -p "ChromeDriver already exists. Do you want to remove it and download a new one? (y/n) " -n 1 -r
  echo
  if [[ $REPLY =~ ^[Yy]$ ]]; then
    rm -f chromedriver
  else
    echo "ChromeDriver not downloaded."
    exit 0
  fi
fi

# Check the OS and architecture and download the correct ChromeDriver
download_url=""
if [ "$os" == "Linux" ]; then
  if [ "$arch" == "x86_64" ]; then
    download_url="https://edgedl.me.gvt1.com/edgedl/chrome/chrome-for-testing/$latest_version/linux64/chromedriver-linux64.zip"
  else
    # As per the current data, there's no linux32 URL provided.
    # Placeholder code for future if such URL becomes available.
    echo "Unsupported architecture."
    exit 1
  fi
elif [ "$os" == "Darwin" ]; then
  # Check if the Mac architecture is ARM64 or x64
  if [ "$arch" == "arm64" ]; then
    download_url="https://edgedl.me.gvt1.com/edgedl/chrome/chrome-for-testing/$latest_version/mac-arm64/chromedriver-mac-arm64.zip"
  else
    download_url="https://edgedl.me.gvt1.com/edgedl/chrome/chrome-for-testing/$latest_version/mac-x64/chromedriver-mac-x64.zip"
  fi
else
  # For Windows (this shell script won't generally run on Windows, but the logic is included for completeness)
  if [ "$arch" == "x86_64" ]; then
    download_url="https://edgedl.me.gvt1.com/edgedl/chrome/chrome-for-testing/$latest_version/win64/chromedriver-win64.zip"
  else
    download_url="https://edgedl.me.gvt1.com/edgedl/chrome/chrome-for-testing/$latest_version/win32/chromedriver-win32.zip"
  fi
fi

# Download the ChromeDriver
if [ ! -z "$download_url" ]; then
  download_chromedriver "$download_url"
  echo "ChromeDriver version $latest_version downloaded."
else
  echo "Error: Couldn't determine the appropriate ChromeDriver download URL."
  exit 1
fi
