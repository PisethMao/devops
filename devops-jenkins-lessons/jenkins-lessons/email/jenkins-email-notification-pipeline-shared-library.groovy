@Library('jenkins-shared-library-fork@master')_

pipeline {
    agent any

    stages {
        stage('Git Checkout') {
            steps {
                sh "echo 'Checkout Completed'"
            }
        }

        stage('Build') {
            steps {
                sh "echo 'Build Completed'"
            }
        }
    }

    post {
        always {
            script {
                emailNotification('pisethmao2002@gmail.com')
            }
        }
    }
}