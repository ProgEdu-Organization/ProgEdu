pipeline {
    agent any
    
    environment {
        NEXT_CLOUD_USERNAME = credentials('next-cloud-username')
        NEXT_CLOUD_PASSWORD = credentials('next-cloud-password')
    }
    
    
    stages {
        
        stage('download .env file') {
            steps {
                script {
                    sh "curl -u $NEXT_CLOUD_USERNAME:$NEXT_CLOUD_PASSWORD --insecure -f $NEXT_CLOUD_HOST/ProgEdu-CI-CD/$BRANCH_NAME/.env -X GET -O"
                }
            }
        }
        
        stage('check .env Exists file') {
            steps {
                script {
                    if( ! fileExists(".env") ) {
                        error('not exists .env file')
                    }
                }
            }
        }

        stage('build') {
            steps {
                sh "docker-compose pull && docker-compose --compatibility -p $BRANCH_NAME-progedu up -d --build"
            }
        }
    }
}