pipeline {

    agent any

    environment {
        SEMGREP = 'C:\\Users\\Abhij\\AppData\\Local\\Programs\\Python\\Python313\\Scripts\\semgrep.exe'
        GITLEAKS = 'E:\\SecureDevOps-Lab\\tools\\gitleaks\\gitleaks.exe'
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
        stage('Gitleaks Scan') {
            steps {
            bat '"%GITLEAKS%" detect --no-git --source . --report-format json --report-path gitleaks-report.json'
            }
        }
        stage('Build') {
            steps {
                bat 'mvn clean install'
            }
        }

        stage('Archive') {
            steps {
                archiveArtifacts artifacts: 'target/*.jar, semgrep-report.json, gitleaks-report.json', fingerprint: true
            }
        }
    }
}