pipeline {
    agent any

    stages {
        stage('Clone Code') {
            steps {
                git 'https://github.com/PisethMao/reactjs-template-product'
                sh 'ls -la'
                sh 'pwd'
                sh '''
                    echo "Changing directory to reactjs-template-product"
                    ls -la
                '''
            }
        }

        stage('Build') {
            steps {
                sh '''
                    docker build -t jenkins-react-pipeline .
                '''
            }
        }

        stage('Deploy') {
            steps {
                sh '''
                    docker stop reactjs-cont || true
                    docker rm -f reactjs-cont || true
                    docker run -d -p 3000:8080 --name reactjs-cont jenkins-react-pipeline
                '''
            }
        }

        stage('Add Domain Name') {
            steps {
                sh '''
                    echo "Running shellscript to add the domain name for the service."
                '''
            }
        }
    }
}