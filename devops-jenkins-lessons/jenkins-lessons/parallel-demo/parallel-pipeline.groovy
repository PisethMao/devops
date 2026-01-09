pipeline {
    agent any

    stages {
        stage('Work 1') {
            steps {
                echo 'Completing the Work 1!!!'
            }
        }

        stage('Work 2') {
            parallel {
                stage('Step 1') {
                    steps {
                        echo 'Step 1'
                    }
                }

                stage('Step 2') {
                    steps {
                       echo 'Step 2' 
                    }
                }
            }
        }

        stage('Work 3') {
            steps {
                echo 'Completing the Work 3!!!'
            }
        }
    }
}