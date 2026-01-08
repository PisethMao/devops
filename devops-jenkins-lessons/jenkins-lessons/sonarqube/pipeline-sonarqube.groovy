def sendTelegramMessage(String message) { 
    withEnv(["TG_MESSAGE=${message}"]) { 
        sh '''#!/bin/bash -e 
            curl -s -X POST "https://api.telegram.org/bot$TOKEN/sendMessage" -d "chat_id=$CHAT_ID" --data-urlencode "text=$TG_MESSAGE" > /dev/null
        ''' 
    } 
}

pipeline {
    agent any

    stages {
        // stage('Telegram Message') {
        //     steps {
        //         withCredentials([usernamePassword(credentialsId: 'TELEGRAM_BOTS', passwordVariable: 'TOKEN', usernameVariable: 'CHAT_ID')]) {
        //             // some block
        //             script {
        //                 sendTelegramMessage("Telegram Pipeline Function")
        //             }
        //             // sh """
        //             //     curl -s -X POST "https://api.telegram.org/bot${TOKEN}/sendMessage" \
        //             //     -d "chat_id=${CHAT_ID}" \
        //             //     --data-urlencode "text=Hello from Jenkins!"
        //             // """
        //         }
        //     }
        // }

        stage('Clone Reactjs Code') {
            steps {
                git 'https://github.com/PisethMao/reactjs-template-product'
            }
        }

        stage('Check Code Quality in SonarQube') {
            environment {
                scannerHome = tool 'sonarqube-scanner'
            }

            steps {
                withSonarQubeEnv(credentialsId: 'SONARQUBE_TOKEN', installationName: 'sonarqube-scanner') {
                    script {
                        def projectKey = 'reactjs-template-product'
                        def projectName = 'ReactjsTemplateProduct'
                        def projectVersion = '1.0.0'
                        sh """
                            ${scannerHome}/bin/sonar-scanner \
                            -Dsonar.projectKey=${projectKey} \
                            -Dsonar.projectName=${projectName} \
                            -Dsonar.projectVersion=${projectVersion}
                        """
                    }
                }
            }
        }

        stage('Wait for Quality Gate') {
            steps {
                // script {
                //     def qg = waitForQualityGate()
                //     if (qg.status != 'OK') {
                //         currentBuild.result = 'FAILURE'
                //         echo "Quality gate failed: ${qg.status}. Stopping pipeline"
                //         return
                //     }
                //     echo "Quality gate passed!"
                //     currentBuild.result = 'SUCCESS'
                // }
                timeout(time: 10, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                }
            }
        }

        stage('Build') {
            when {
                expression {
                    currentBuild.result == 'SUCCESS'
                }
            }
            steps {
                echo 'Building the docker image'
            }
        }

        stage('Push') {
            when {
                expression {
                    currentBuild.result == 'SUCCESS'
                }
            }
            steps {
                echo 'Pushing the docker image to registry'
            }
        }
    }

    post {
        success {
            withCredentials([usernamePassword(credentialsId: 'TELEGRAM_BOTS', passwordVariable: 'TOKEN', usernameVariable: 'CHAT_ID')]) {
                // some block
                script {
                    sendTelegramMessage("Deployment is Success!!!")
                }
                // sh """
                //     curl -s -X POST "https://api.telegram.org/bot${TOKEN}/sendMessage" \
                //     -d "chat_id=${CHAT_ID}" \
                //     --data-urlencode "text=Hello from Jenkins!"
                // """
            }
        }

        failure {
            withCredentials([usernamePassword(credentialsId: 'TELEGRAM_BOTS', passwordVariable: 'TOKEN', usernameVariable: 'CHAT_ID')]) {
                // some block
                script {
                    sendTelegramMessage("Deployment is Failed!!!")
                }
                // sh """
                //     curl -s -X POST "https://api.telegram.org/bot${TOKEN}/sendMessage" \
                //     -d "chat_id=${CHAT_ID}" \
                //     --data-urlencode "text=Hello from Jenkins!"
                // """
            }
        }
    }
}
