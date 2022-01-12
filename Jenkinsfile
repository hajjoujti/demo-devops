pipeline {
    agent any

    tools {
        maven 'M3'
    }

    stages {
        stage('Checkout') {
            steps {
                echo "---|CHECKOUT STAGE|---"
                git branch: "master", url:'https://github.com/hajjoujti/demo-devops.git'
            }
        }

        stage('Compile') {
            steps {
                echo "---|COMPILATION STAGE|---"
                sh 'mvn compile'
            }
        }

        stage('Test') {
            steps {
                echo "---|TEST STAGE|---"
                sh 'mvn test'
            }

            post {
                success {
                    junit 'target/surefire-reports/*.xml'
                }
            }
        }

        stage('Package') {
            steps {
                echo "---|PACKAGING STAGE|---"
                sh 'mvn clean package -DskipTest'
            }

            post {
                always {
                    archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
                }

                success {
                    junit 'target/surefire-reports/*.xml'
                }
            }
        }

        stage('SSH transfer') {
            steps {
                echo "---|TRANSFER STAGE|---"
                script {
                    sshPublisher(publishers: [
                        sshPublisherDesc(configName: 'docker-host', transfers:[
                            sshTransfer(
                                execCommand: '''
                                    sudo docker stop demo || true;
                                    sudo docker rm demo || true;
                                    sudo rmi demo || true;
                                '''
                            ),
                            sshTransfer(
                                sourceFiles:"target/*.jar",
                                removePrefix: "target",
                                remoteDirectory: "//home//vagrant",
                                execCommand: "ls /home/vagrant;"
                            ),
                            sshTransfer(
                                sourceFiles:"Dockerfile",
                                removePrefix: "",
                                remoteDirectory: "//home//vagrant",
                                execCommand: '''
                                    cd /home/vagrant;
                                    sudo docker build -t demo .;
                                    sudo docker run -d --name demo -p 8080:8080 demo;
                                '''
                            )
                        ])
                    ])
                }
            }
        }
    }
}
