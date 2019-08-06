pipeline {
    agent any
    tools {
        maven 'Maven'
        jdk 'JAVA-jdk'
    }
    stages {
        stage ('Initialize-Variables') {
            steps {
                bat '''
                    echo "PATH = %PATH%"
                    echo "M2_HOME = %M2_HOME%"
                '''
            }
        }
        stage ('Build') {
            steps {
                    bat 'cd ProjectManagement && mvn install'
            }
        }
        stage ('Docker-Build') {
            steps {
                    bat 'cd ProjectManagement && mvn package docker:build'
            }
        }
    }
}