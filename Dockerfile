# Use the official Jenkins LTS image with JDK 17
FROM jenkins/jenkins:alpine

# Copy the plugins.txt file to the Jenkins reference directory
COPY --chown=jenkins:jenkins plugins.txt /usr/share/jenkins/ref/plugins.txt

# Run the jenkins-plugin-cli to install plugins listed in plugins.txt
#RUN jenkins-plugin-cli -f /usr/share/jenkins/ref/plugins.txt && \
#apt install tree && \
#alias ls='ls --color=auto'