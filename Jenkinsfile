pipeline {
    agent any

    tools {
        maven 'maven3.9'
    }

    environment {
        IMAGE_NAME = "security-app"
        IMAGE_TAG  = "latest"
    }

    stages {

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build') {
            steps {
                sh 'mvn clean install'
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    withCredentials([usernamePassword(
                        credentialsId: 'f884ea78-b111-4d22-8445-4863584d4c76',
                        usernameVariable: 'DOCKER_USER',
                        passwordVariable: 'DOCKER_PASS'
                    )]) {

                        env.DOCKER_IMAGE = "${DOCKER_USER}/${IMAGE_NAME}:${IMAGE_TAG}"

                        sh '''
#                            set +x
                            docker build -t $DOCKER_IMAGE .
                        '''
                    }
                }
            }
        }

        stage('Push Docker Image') {
            steps {
                script {
                    withCredentials([usernamePassword(credentialsId: 'd618be56-b0bb-4b74-b2a8-6278a7dfb860', passwordVariable: 'DOCKER_PASS', usernameVariable: 'DOCKER_USER')]) {
                         env.DOCKER_IMAGE = "${DOCKER_USER}/${IMAGE_NAME}:${IMAGE_TAG}"

                         sh '''
#                            set +x
                             echo "$DOCKER_PASS" | docker login -u "$DOCKER_USER" --password-stdin
                             docker push "$DOCKER_IMAGE"
                             docker logout
                            '''
                    }
                }
            }
        }
    }
    post {
        always {
            cleanWs()
        }
    }
}