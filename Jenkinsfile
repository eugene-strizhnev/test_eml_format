#!Groovy

targetRegion = "us-east-1"

ecsTemplateDir = "ecs"


nodeId = platformDefaults.getBuildNodeLabel()

withNodeWrapper(nodeId) {

    stage("Checkout") {
        deleteDir()
        checkout scm
    }

    /*
     * Run gradle build tasks -- gradle assemble, test, build
     * Please ensure that gradle build invokes all of your gradle tasks
     * Stages: Compile, Test, Build
     */
    gradleBuild()

    //Create Docker image and upload to ECR
    dockerBuildPush()

    if (env.BRANCH_NAME == "master") {
        stage('Analyzing Source Code - SonarQube') {
            sh '/opt/gradle/bin/gradle sonarqube'
        }
    }

    def ecsDeployParametersPath = "${ecsTemplateDir}/parameters.json"
    def ecsDeployDevParametersPath = "${ecsTemplateDir}/devqa_parameters.json"

    def deploymentsInfo = [:]

    withDeployWrapper('devqa') {
        //runIntegrationTests('devqa')
        //ecsDeploy deployEnv: 'devqa', parametersPath: ecsDeployDevParametersPath
        //performRelease releaseMethod: 'fast', deployEnv: 'devqa'
        //deploymentsInfo.put("devqa", getBuildInfo("devqa"))
    }

}
