pipeline {
    agent any

    stages {
        stage('Code Checkout') {
            steps {
                sh "echo 'Checkout Completed'"
            }
        }

        stage('Build') {
            steps {
                sh "echo 'Build Completed'"
            }
        }

        stage('Test') {
            steps {
                sh "exit 1"
            }
        }
    }

    post {
        always {
            emailext (
                to: 'pisethmao2002@gmail.com',
                subject: "Jenkins Build Notification: ${currentBuild.currentResult}",
                body: """\
                    Build Status: ${currentBuild.currentResult}
                    Project: ${env.JOB_NAME}
                    Build Number: ${env.BUILD_NUMBER}
                    Build URL: ${env.BUILD_URL}
                """
            )
        }
    }
}