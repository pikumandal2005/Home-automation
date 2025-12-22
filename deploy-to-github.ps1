# 🚀 GitHub Deployment Script
# Run this script to push your project to GitHub and enable GitHub Pages

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "  Smart Home Automation - GitHub Setup" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# Check if Git is installed
if (-not (Get-Command git -ErrorAction SilentlyContinue)) {
    Write-Host "❌ Git is not installed!" -ForegroundColor Red
    Write-Host "Please install Git from: https://git-scm.com/" -ForegroundColor Yellow
    exit 1
}

Write-Host "✅ Git is installed" -ForegroundColor Green

# Check if we're in a git repository
if (-not (Test-Path ".git")) {
    Write-Host ""
    Write-Host "📝 Initializing Git repository..." -ForegroundColor Yellow
    git init
    Write-Host "✅ Git repository initialized" -ForegroundColor Green
}

# Get user confirmation
Write-Host ""
Write-Host "This script will:" -ForegroundColor Yellow
Write-Host "  1. Add all files to Git" -ForegroundColor White
Write-Host "  2. Commit changes" -ForegroundColor White
Write-Host "  3. Push to GitHub" -ForegroundColor White
Write-Host "  4. Provide instructions for GitHub Pages" -ForegroundColor White
Write-Host ""

$confirm = Read-Host "Do you want to continue? (y/n)"
if ($confirm -ne "y") {
    Write-Host "❌ Deployment cancelled" -ForegroundColor Red
    exit 0
}

# Get GitHub repository URL
Write-Host ""
Write-Host "📦 GitHub Repository Setup" -ForegroundColor Cyan
Write-Host "Your repository: https://github.com/pikumandal2005/Home-automation" -ForegroundColor White
Write-Host ""

$repoUrl = "https://github.com/pikumandal2005/Home-automation.git"
Write-Host "Using repository: $repoUrl" -ForegroundColor Green

# Check if remote exists
$remoteExists = git remote get-url origin 2>$null
if (-not $remoteExists) {
    Write-Host "Adding remote origin..." -ForegroundColor Yellow
    git remote add origin $repoUrl
    Write-Host "✅ Remote added" -ForegroundColor Green
} else {
    Write-Host "✅ Remote already exists" -ForegroundColor Green
}

# Stage all files
Write-Host ""
Write-Host "📁 Staging files..." -ForegroundColor Yellow
git add .
Write-Host "✅ Files staged" -ForegroundColor Green

# Commit
Write-Host ""
Write-Host "💾 Committing changes..." -ForegroundColor Yellow
$commitMessage = "docs: Professional GitHub repository setup with comprehensive documentation"
git commit -m "$commitMessage"
Write-Host "✅ Changes committed" -ForegroundColor Green

# Push to GitHub
Write-Host ""
Write-Host "🚀 Pushing to GitHub..." -ForegroundColor Yellow
Write-Host "Note: You may be prompted for GitHub credentials" -ForegroundColor Cyan

# Try to push
try {
    git push -u origin main 2>&1 | Write-Host
    Write-Host "✅ Successfully pushed to GitHub!" -ForegroundColor Green
} catch {
    Write-Host ""
    Write-Host "⚠️  If the push failed, you may need to:" -ForegroundColor Yellow
    Write-Host "  1. Set up a GitHub Personal Access Token" -ForegroundColor White
    Write-Host "  2. Use: git push -u origin main" -ForegroundColor White
    Write-Host "  3. Or use GitHub Desktop: https://desktop.github.com/" -ForegroundColor White
}

# GitHub Pages instructions
Write-Host ""
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "  GitHub Pages Deployment" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "To enable GitHub Pages for your website:" -ForegroundColor Yellow
Write-Host ""
Write-Host "1. Go to: https://github.com/pikumandal2005/Home-automation/settings/pages" -ForegroundColor White
Write-Host ""
Write-Host "2. Under 'Source':" -ForegroundColor White
Write-Host "   - Select: 'Deploy from a branch'" -ForegroundColor Cyan
Write-Host "   - Branch: 'main'" -ForegroundColor Cyan
Write-Host "   - Folder: '/ (root)'" -ForegroundColor Cyan
Write-Host ""
Write-Host "3. Click 'Save'" -ForegroundColor White
Write-Host ""
Write-Host "4. Wait 2-3 minutes for deployment" -ForegroundColor White
Write-Host ""
Write-Host "5. Your website will be live at:" -ForegroundColor Green
Write-Host "   https://pikumandal2005.github.io/Home-automation/" -ForegroundColor Cyan
Write-Host ""

# Repository view instructions
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "  Next Steps" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "✅ Add screenshots to docs/screenshots/ folder" -ForegroundColor White
Write-Host "✅ Update Firebase credentials in index.html" -ForegroundColor White
Write-Host "✅ Build and upload Android APK to Releases" -ForegroundColor White
Write-Host "✅ Test website after GitHub Pages deployment" -ForegroundColor White
Write-Host "✅ Share your project link!" -ForegroundColor White
Write-Host ""

Write-Host "Repository URL: https://github.com/pikumandal2005/Home-automation" -ForegroundColor Green
Write-Host "Website URL: https://pikumandal2005.github.io/Home-automation/" -ForegroundColor Green
Write-Host ""
Write-Host "🎉 Deployment complete! Your project is now professional!" -ForegroundColor Cyan
Write-Host ""
