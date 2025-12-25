# =============================================================================
# SCRIPT DE REORGANISATION DU PROJET YD_BANK
# =============================================================================

Write-Host "Starting YD_Bank reorganization..." -ForegroundColor Cyan

# 0. Stop processes
Stop-Process -Name "java" -ErrorAction SilentlyContinue
Stop-Process -Name "node" -ErrorAction SilentlyContinue

# Paths
$CurrentDir = Get-Location
$ParentDir = Split-Path $CurrentDir -Parent

Write-Host "Current Directory (Backend): $CurrentDir"
Write-Host "Parent Directory (Root): $ParentDir"

# 1. Create target directories
$DirsToCreate = @("frontend", "database", "documentation", "scripts")
foreach ($Dir in $DirsToCreate) {
    $Path = Join-Path $ParentDir $Dir
    if (-not (Test-Path $Path)) {
        New-Item -ItemType Directory -Force -Path $Path | Out-Null
        Write-Host "   + Created: $Dir" -ForegroundColor Green
    }
}

# 2. Move Frontend
$FrontendSrc = Join-Path $CurrentDir "ebank-frontend"
$FrontendDest = Join-Path $ParentDir "frontend"
if (Test-Path $FrontendSrc) {
    Write-Host "Moving Frontend..."
    # Move content
    Get-ChildItem -Path $FrontendSrc | Move-Item -Destination $FrontendDest -Force
    Remove-Item -Path $FrontendSrc -Force -Recurse -ErrorAction SilentlyContinue
    Write-Host "   -> Frontend moved to 'frontend/'" -ForegroundColor Green
}

# 3. Move Database
$DbSrc = Join-Path $CurrentDir "database"
$DbDest = Join-Path $ParentDir "database"
if (Test-Path $DbSrc) {
    Write-Host "Moving Database..."
    Get-ChildItem -Path $DbSrc | Move-Item -Destination $DbDest -Force
    Remove-Item -Path $DbSrc -Force -Recurse -ErrorAction SilentlyContinue
    Write-Host "   -> Database moved to 'database/'" -ForegroundColor Green
}

# 4. Move Scripts
$ScriptsDest = Join-Path $ParentDir "scripts"
Write-Host "Moving Scripts..."
Get-ChildItem -Path $CurrentDir -Filter "*.ps1" | Where-Object { $_.Name -ne "reorganize-project.ps1" } | Move-Item -Destination $ScriptsDest -Force
Get-ChildItem -Path $CurrentDir -Filter "*.sh" | Move-Item -Destination $ScriptsDest -Force
Write-Host "   -> Scripts moved to 'scripts/'" -ForegroundColor Green

# 5. Move Documentation
$DocDest = Join-Path $ParentDir "documentation"
Write-Host "Moving Documentation..."
Get-ChildItem -Path $CurrentDir -Filter "*.md" | Where-Object { $_.Name -ne "README.md" } | Move-Item -Destination $DocDest -Force
Write-Host "   -> Docs moved to 'documentation/'" -ForegroundColor Green

# 6. Manage Root README
if (Test-Path (Join-Path $CurrentDir "README_ROOT.md")) {
    Move-Item -Path (Join-Path $CurrentDir "README_ROOT.md") -Destination (Join-Path $ParentDir "README.md") -Force
}

# 7. Cleanup
Write-Host "Cleanup..."
Remove-Item -Path (Join-Path $CurrentDir "scripts") -Force -Recurse -ErrorAction SilentlyContinue

Write-Host "REORGANIZATION COMPLETE!" -ForegroundColor Cyan
Write-Host "   New structure :"
Write-Host "   $ParentDir\backend   (Spring Boot Code)"
Write-Host "   $ParentDir\frontend  (React Code)"
Write-Host "   $ParentDir\database  (Database)"
Write-Host "   $ParentDir\scripts   (Demo Tools)"
