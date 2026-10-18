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
    }

    post {
        success {
            echo "Deployed. Open http://localhost:8081/catalogue-${params.DEPLOY_ENV}/"
        }
        failure {
            echo 'Pipeline failed. Check the stage that turned red.'
        }
    }
}