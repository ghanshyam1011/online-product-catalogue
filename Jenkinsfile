pipeline {
    agent any

    parameters {
        choice(name: 'DEPLOY_ENV', choices: ['dev', 'staging'],
               description: 'Target environment. It decides the context path on Tomcat')
        string(name: 'TOMCAT_WEBAPPS', defaultValue: 'C:\\Tools\\tomcat\\webapps',
               description: 'Tomcat webapps folder')
    }

    environment {
        JAVA_HOME  = 'C:\\Program Files\\Java\\jdk-17.0.20'
        MAVEN_HOME = 'C:\\Tools\\apache-maven-3.10.0'
        PATH       = "${JAVA_HOME}\\bin;${MAVEN_HOME}\\bin;${env.PATH}"
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build') {
            steps {
                bat 'mvn -B clean compile'
            }
        }

        stage('Test') {
            steps {
                bat 'mvn -B test'
            }
            post {
                always {
                    junit testResults: 'target/surefire-reports/*.xml', allowEmptyResults: true
                }
            }
        }

        stage('Package') {
            steps {
                bat 'mvn -B package -DskipTests'
            }
            post {
                success {
                    archiveArtifacts artifacts: 'target/*.war', fingerprint: true
                }
            }
        }

        stage('Deploy') {
            steps {
                bat '''
                    for %%f in (target\\*.war) do copy /Y "%%f" "%TOMCAT_WEBAPPS%\\catalogue-%DEPLOY_ENV%.war"
                '''
            }
        }

        stage('Verify') {
            steps {
                powershell '''
                    $url = "http://localhost:8081/catalogue-$env:DEPLOY_ENV/login"
                    for ($i = 0; $i -lt 36; $i++) {
                        try {
                            $r = Invoke-WebRequest -Uri $url -UseBasicParsing -TimeoutSec 5
                            if ($r.StatusCode -eq 200) {
                                Write-Host "App is up at $url"
                                exit 0
                            }
                        } catch {
                            Write-Host "Waiting for $url ..."
                        }
                        Start-Sleep -Seconds 5
                    }
                    Write-Host "App did not come up at $url"
                    exit 1
                '''
            }
        }

        stage('Docker Build') {
            steps {
                bat 'docker build -t product-catalogue:%BUILD_NUMBER% -t product-catalogue:latest .'
            }
        }

        stage('Docker Deploy') {
            steps {
                withCredentials([
                    string(credentialsId: 'catalogue-admin-password', variable: 'ADMIN_PASSWORD'),
                    string(credentialsId: 'catalogue-user-password', variable: 'USER_PASSWORD')
                ]) {
                    // -e NAME without a value passes the variable from this process,
                    // so the passwords never appear on the command line or in the image.
                    bat '''
                        docker rm -f catalogue-docker 2>nul
                        docker run -d --name catalogue-docker -p 8082:8080 -e ADMIN_PASSWORD -e USER_PASSWORD product-catalogue:%BUILD_NUMBER%
                    '''
                }
            }
        }

        stage('Docker Verify') {
            steps {
                powershell '''
                    $url = "http://localhost:8082/login"
                    for ($i = 0; $i -lt 36; $i++) {
                        try {
                            $r = Invoke-WebRequest -Uri $url -UseBasicParsing -TimeoutSec 5
                            if ($r.StatusCode -eq 200) {
                                Write-Host "Container is up at $url"
                                exit 0
                            }
                        } catch {
                            Write-Host "Waiting for $url ..."
                        }
                        Start-Sleep -Seconds 5
                    }
                    docker logs --tail 50 catalogue-docker
                    Write-Host "Container did not come up at $url"
                    exit 1
                '''
            }
        }
    }

    post {
        success {
            echo "Deployed. Tomcat: http://localhost:8081/catalogue-${params.DEPLOY_ENV}/  Docker: http://localhost:8082/"
        }
        failure {
            echo 'Pipeline failed. Check the stage that turned red.'
        }
    }
}