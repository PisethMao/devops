pipeline {
    agent any

    stages {
        stage('Clone Code') {
            steps {
                git 'https://github.com/sexymanalive/reactjs-devop10-template'
            }
        }

        stage('Build Image') {
            steps {
                sh '''
                    docker build -t react-jenkins-itp .
                '''
            }
        }

        stage('Run Service') {
            steps {
                sh '''
                    docker stop react-app || true
                    docker rm -f react-app || true
                    docker run -d -p 3002:8080 --name reactjs-cont-2 react-jenkins-itp
                '''
            }
        }

        stage('Push Image') {
            steps {
                sh '''
                    echo "Pushing image to docker hub."
                '''
            }
        }
    }
}