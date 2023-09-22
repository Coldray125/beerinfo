#!/bin/bash

# Variable declaration
jenkins_url="$JENKINS_URL"
jar_path="/tmp/jenkins-cli.jar"
jenkins_container="jenkins"

# Logging function
log() {
    local message="$1"
    echo "[INFO] $message"
}

echo "$jenkins_url"

# Function to download jenkins-cli.jar
download_jar() {
log "Downloading jenkins-cli.jar..."
docker exec -i "$jenkins_container"  curl -o "$jar_path" "${jenkins_url}jnlpJars/jenkins-cli.jar"
}

install_plugin() {
    local plugin_name="$1"
    log "Installing Jenkins plugin: $plugin_name"
    if ! docker exec -i "$jenkins_container" java -jar "$jar_path" -s "$jenkins_url" install-plugin "$plugin_name"; then
        log "Failed to install $plugin_name plugin"
        exit 1
    fi
}

# Download jenkins-cli.jar
download_jar

# Install Configuration as Code (JCasC) plugin
install_plugin "configuration-as-code"

# Install Allure plugin
install_plugin "allure-jenkins-plugin"

# Install Allure plugin
install_plugin "workflow-aggregator"

log "Safe-restarting Jenkins..."
if ! docker exec -i "$jenkins_container"  java -jar "$jar_path" -s "$jenkins_url" safe-restart; then
    log "Failed to safely restart Jenkins"
    exit 1
fi

log "Script completed successfully"