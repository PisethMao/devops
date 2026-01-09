pipeline {
    agent any

    stages {
        stage('Parallel') {
            parallel {
                stage('Work 1') {
                    steps {
                        echo 'Completing the work 1'
                    }
                }

                stage('Work 1') {
                    steps {
                        echo 'Completing the work 1'
                    }
                }
            }
        }
    }
}