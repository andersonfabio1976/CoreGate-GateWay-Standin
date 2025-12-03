# ================================
# DEPLOY DE TODOS OS MÓDULOS CORE-GATE
# PowerShell Script
# ================================

Write-Host "=== STARTING DEPLOY CORE-GATE ===" -ForegroundColor Cyan

function BuildAndRunModule {
    param (
        [string]$Folder,
        [string]$Name,
        [int]$Port
    )

    Write-Host "`n===============================" -ForegroundColor Yellow
    Write-Host " DEPLOYING: $Name" -ForegroundColor Yellow
    Write-Host "===============================" -ForegroundColor Yellow

    Push-Location $Folder

    # ===================
    # MAVEN BUILD
    # ===================
    try {
        Write-Host "→ mvn clean package"
        mvn clean package -DskipTests
    }
    catch {
        Write-Host "⚠ WARNING: Maven returned an error, continuing..." -ForegroundColor Red
    }

    # ===================
    # STOP / REMOVE OLD CONTAINER
    # ===================
    Write-Host "→ Stopping container $Name (if exists)"
    docker stop $Name | Out-Null 2>&1

    Write-Host "→ Removing container $Name (if exists)"
    docker rm $Name | Out-Null 2>&1

    # ===================
    # DOCKER BUILD
    # ===================
    Write-Host "→ docker build"
    try {
        docker build -t "$Name:latest" .
    }
    catch {
        Write-Host "⚠ WARNING: Docker build returned an error, continuing..." -ForegroundColor Red
    }

    # ===================
    # DOCKER RUN
    # ===================
    Write-Host "→ docker run on port $Port"
    try {
        docker run -d `
            --name $Name `
            -p $Port:8080 `
            "$Name:latest"
    }
    catch {
        Write-Host "⚠ WARNING: Docker run failed." -ForegroundColor Red
    }

    Pop-Location
}

# ================================
# LISTA DOS MÓDULOS PARA DEPLOY
# (AJUSTEI AS PORTAS PARA NÃO COLIDIR)
# ================================

BuildAndRunModule -Folder "ingress"      -Name "coregate-ingress"      -Port 8082
BuildAndRunModule -Folder "context"      -Name "coregate-context"      -Port 8083
BuildAndRunModule -Folder "orchestrator" -Name "coregate-orchestrator" -Port 8084
BuildAndRunModule -Folder "finalizer"    -Name "coregate-finalizer"    -Port 8085
BuildAndRunModule -Folder "mockemissor"  -Name "coregate-mockissuer"   -Port 8086

Write-Host "`n=== DEPLOY COMPLETED ===" -ForegroundColor Green
