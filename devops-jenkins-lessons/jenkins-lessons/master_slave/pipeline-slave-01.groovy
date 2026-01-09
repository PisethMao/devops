pipeline {
    agent any

    stages {
        stage('Work 1') {
            agent {
                label 'Worker-01'
            }

            steps {
                sh '''
                    pwd
                    curl ifconfig.me
                    whoami
                '''
            }
        }

        stage('Work 2') {
            agent {
                label 'master-node'
            }

            steps {
                sh '''
                    pwd
                    curl ifconfig.me
                    whoami
                '''
            }
        }
    }
}