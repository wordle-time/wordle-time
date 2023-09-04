#-------------------------------------------------------------------------------------------------------------
# Copyright (c) Microsoft Corporation. All rights reserved.
# Licensed under the MIT License. See https://go.microsoft.com/fwlink/?linkid=2090316 for license information.
#-------------------------------------------------------------------------------------------------------------

FROM mcr.microsoft.com/vscode/devcontainers/base:ubuntu

RUN apt-get update && export DEBIAN_FRONTEND=noninteractive \
    && apt-get -y install default-jre graphviz libgtk2.0-0 libgtk-3-0 libgbm-dev libnotify-dev libgconf-2-4 libnss3 libxss1 libasound2 libxtst6 xauth xvfb
    # && npm install -g prettier
#RUN apt-get update 
    # && apt-get -y install default-jre graphviz libgtk2.0-0 libgtk-3-0 libgbm-dev libnotify-dev libgconf-2-4 libnss3 libxss1 libasound2 libxtst6 xauth xvfb \
    # && npm install -g prettier \
    # && npm install \
    # && ./node_modules/.bin/cypress install

RUN apt-get update && export DEBIAN_FRONTEND=noninteractive \
    && apt-get -y install texlive-latex-base texlive-fonts-recommended texlive-extra-utils texlive-latex-extra