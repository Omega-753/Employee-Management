pipeline {

    agent any

    environment {
        SEMGREP = 'C:\\Users\\Abhij\\AppData\\Local\\Programs\\Python\\Python313\\Scripts\\semgrep.exe'
    }

    stages {

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Semgrep Scan') {
            steps {
                bat '"%SEMGREP%" --config p/java --error --json --output semgrep-report.json .'
            }
        }

        stage('Build') {
            steps {
                bat 'mvn clean install'
            }
        }

        stage('Archive') {
            steps {
                archiveArtifacts artifacts: 'target/*.jar, semgrep-report.json', fingerprint: true
            }
        }
    }
}