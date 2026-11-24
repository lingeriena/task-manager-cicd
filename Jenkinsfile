pipeline {
    agent any
    options {
        timestamps()
        disableConcurrentBuilds()
        buildDiscarder(logRotator(numToKeepStr: '20'))
    }
    stages {
        stage('Fix Git Ownership') {
            steps {
                bat 'git config --global --add safe.directory C:/repos/task-manager-cicd.git'
            }
        }
        stage('Checkout') {
            steps {
                checkout scm
            }
        }
        stage('Toolchain') {
            steps {
                bat 'java -version'
                bat 'mvn -version'
                bat 'git --version'
            }
        }
        stage('Unit Tests') {
            steps {
                bat 'mvn -B -ntp clean test'
            }
        }
        stage('Integration Tests') {
            steps {
                bat 'mvn -B -ntp failsafe:integration-test failsafe:verify -Dsurefire.skip=true'
            }
        }
        stage('Package & Reports') {
            steps {
                bat 'mvn -B -ntp package -DskipTests'
                bat 'mvn -B -ntp surefire-report:report-only'
                bat 'mvn -B -ntp jacoco:report'
            }
        }
    }
    post {
        always {
            junit(allowEmptyResults: true, testResults: 'target/surefire-reports/TEST-*.xml,target/failsafe-reports/TEST-*.xml')
            archiveArtifacts(artifacts: 'target/surefire-reports/**,target/failsafe-reports/**,target/site/**', allowEmptyArchive: true)
        }
        success {
            archiveArtifacts(artifacts: 'target/task-manager-cicd-1.0.0.jar', fingerprint: true)
        }
    }
}