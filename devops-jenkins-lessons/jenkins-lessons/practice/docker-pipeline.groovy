pipeline {
    agent any

    environment {
        DOCKER_IMAGE = 'myreactjs-image'
        DOCKER_USERNAME = 'pisethmao'
    }

    stages {
        stage('Show ENV Variable') {
            steps {
                sh '''
                    echo "Full image name is: $DOCKER_USERNAME/$DOCKER_IMAGE"
                '''
            }
        }

        stage('Deploy Nginx Container') {
            steps {
                sh '''
                    docker stop nginx-cont || true
                    docker rm nginx-cont || true
                '''
                script {
                    def nginxApp = docker.image('nginx:trixie-perl')
                    nginxApp.inside {
                        sh '''
                            ls -lrt
                            nginx -v
                        '''
                    }
                    nginxApp.run('--name nginx-cont -dp 8081:80')
                }
            }
        }
    }
}