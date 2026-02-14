pipeline {
    agent any

    tools {
        maven 'maven3.9'
    }

    environment {
        IMAGE_NAME = "security-app"
        IMAGE_TAG  = "${BUILD_NUMBER}"
        K8S_NAMESPACE = "default"
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
                        credentialsId: 'd618be56-b0bb-4b74-b2a8-6278a7dfb860',
                        usernameVariable: 'DOCKER_USER',
                        passwordVariable: 'DOCKER_PASS'
                    )]) {

                        env.DOCKER_IMAGE = "${DOCKER_USER}/${IMAGE_NAME}:${IMAGE_TAG}"

                        sh '''
                            docker build -t $DOCKER_IMAGE .
                        '''
                    }
                }
            }
        }

        stage('Push Docker Image') {
            steps {
                script {
                    withCredentials([usernamePassword(
                        credentialsId: 'd618be56-b0bb-4b74-b2a8-6278a7dfb860',
                        usernameVariable: 'DOCKER_USER',
                        passwordVariable: 'DOCKER_PASS'
                    )]) {

                        env.DOCKER_IMAGE = "${DOCKER_USER}/${IMAGE_NAME}:${IMAGE_TAG}"

                        sh '''
                            echo "$DOCKER_PASS" | docker login -u "$DOCKER_USER" --password-stdin
                            docker push "$DOCKER_IMAGE"
                            docker logout
                        '''
                    }
                }
            }
        }

        stage('Deploy to Minikube') {
            steps {
                script {
                    withCredentials([usernamePassword(
                        credentialsId: 'd618be56-b0bb-4b74-b2a8-6278a7dfb860',
                        usernameVariable: 'DOCKER_USER',
                        passwordVariable: 'DOCKER_PASS'
                    )]) {

                        env.DOCKER_IMAGE = "${DOCKER_USER}/${IMAGE_NAME}:${IMAGE_TAG}"

                        sh '''
                            # Use minikube context
                            kubectl config use-context minikube

                            # Create deployment if it doesn't exist
                            kubectl get deployment security-app -n $K8S_NAMESPACE || \
                            kubectl create deployment security-app \
                                --image=$DOCKER_IMAGE \
                                -n $K8S_NAMESPACE

                            # Expose service if not exists
                            kubectl get service security-app-service -n $K8S_NAMESPACE || \
                            kubectl expose deployment security-app \
                                --type=NodePort \
                                --port=80 \
                                --target-port=8080 \
                                --name=security-app-service \
                                -n $K8S_NAMESPACE

                            # Update image (rolling update)
                            kubectl set image deployment/security-app \
                                security-app=$DOCKER_IMAGE \
                                -n $K8S_NAMESPACE

                            # Wait for rollout
                            kubectl rollout status deployment/security-app \
                                -n $K8S_NAMESPACE
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
