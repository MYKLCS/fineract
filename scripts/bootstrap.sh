#!/bin/bash
set -e

echo "🚀 Fineract Rust/TypeScript Migration Bootstrap"
echo "================================================"

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Function to print colored output
print_status() {
    echo -e "${BLUE}[INFO]${NC} $1"
}

print_success() {
    echo -e "${GREEN}[SUCCESS]${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

print_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# Check if running in the correct directory
if [[ ! -f "MIGRATION_PLAN.md" ]]; then
    print_error "Please run this script from the project root directory"
    exit 1
fi

print_status "Checking system dependencies..."

# Check for required tools
check_command() {
    if ! command -v $1 &> /dev/null; then
        print_error "$1 is not installed. Please install it first."
        return 1
    else
        print_success "$1 is available"
        return 0
    fi
}

# Check system dependencies
DEPS_OK=true
check_command "curl" || DEPS_OK=false
check_command "git" || DEPS_OK=false

if [[ "$DEPS_OK" = false ]]; then
    print_error "Some required dependencies are missing. Please install them first."
    exit 1
fi

# Install Rust if not present
if ! command -v rustc &> /dev/null; then
    print_status "Installing Rust..."
    curl --proto '=https' --tlsv1.2 -sSf https://sh.rustup.rs | sh -s -- -y
    source $HOME/.cargo/env
    print_success "Rust installed successfully"
else
    print_success "Rust is already installed"
    rustc --version
fi

# Install Node.js if not present
if ! command -v node &> /dev/null; then
    print_status "Installing Node.js..."
    curl -fsSL https://deb.nodesource.com/setup_20.x | sudo -E bash -
    sudo apt-get install -y nodejs
    print_success "Node.js installed successfully"
else
    print_success "Node.js is already installed"
    node --version
fi

# Install PostgreSQL if not present
if ! command -v psql &> /dev/null; then
    print_status "Installing PostgreSQL..."
    sudo apt-get update
    sudo apt-get install -y postgresql postgresql-contrib libpq-dev
    sudo systemctl start postgresql
    sudo systemctl enable postgresql
    print_success "PostgreSQL installed successfully"
else
    print_success "PostgreSQL is already installed"
fi

# Install Docker if not present
if ! command -v docker &> /dev/null; then
    print_status "Installing Docker..."
    curl -fsSL https://get.docker.com -o get-docker.sh
    sudo sh get-docker.sh
    sudo usermod -aG docker $USER
    rm get-docker.sh
    print_success "Docker installed successfully"
    print_warning "Please log out and back in for Docker group changes to take effect"
else
    print_success "Docker is already installed"
fi

# Install Docker Compose if not present
if ! command -v docker-compose &> /dev/null; then
    print_status "Installing Docker Compose..."
    sudo curl -L "https://github.com/docker/compose/releases/latest/download/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
    sudo chmod +x /usr/local/bin/docker-compose
    print_success "Docker Compose installed successfully"
else
    print_success "Docker Compose is already installed"
fi

# Setup environment file
print_status "Setting up environment configuration..."
if [[ ! -f ".env" ]]; then
    cp .env.example .env
    print_success "Environment file created from template"
    print_warning "Please review and update .env with your specific configuration"
else
    print_warning ".env file already exists, skipping template copy"
fi

# Install Rust dependencies and check compilation
print_status "Setting up Rust backend..."
cd backend
if cargo check; then
    print_success "Rust backend compiles successfully"
else
    print_error "Rust backend compilation failed"
    exit 1
fi

# Install additional Rust tools
print_status "Installing Rust development tools..."
cargo install diesel_cli --no-default-features --features postgres || print_warning "diesel_cli installation failed"
cargo install cargo-watch || print_warning "cargo-watch installation failed"

cd ..

# Setup frontend (when implemented)
if [[ -d "frontend" ]] && [[ -f "frontend/package.json" ]]; then
    print_status "Setting up TypeScript frontend..."
    cd frontend
    npm install
    if npm run build; then
        print_success "Frontend builds successfully"
    else
        print_error "Frontend build failed"
        exit 1
    fi
    cd ..
else
    print_warning "Frontend not yet implemented (will be added in Phase 5)"
fi

# Setup database
print_status "Setting up database..."
if command -v docker &> /dev/null; then
    print_status "Starting PostgreSQL with Docker..."
    docker run --name fineract-postgres -p 5432:5432 -e POSTGRES_PASSWORD=postgres -e POSTGRES_DB=fineract -d postgres:16.1 || print_warning "PostgreSQL container might already be running"
    sleep 5
    
    # Wait for database to be ready
    print_status "Waiting for database to be ready..."
    timeout 30 bash -c 'until docker exec fineract-postgres pg_isready -U postgres; do sleep 1; done'
    
    print_success "PostgreSQL is running in Docker"
else
    print_warning "Docker not available, please set up PostgreSQL manually"
fi

# Run database migrations (when implemented)
print_status "Database migrations will be implemented in Phase 1"

print_success "Bootstrap completed successfully!"
echo ""
echo "🎉 Next Steps:"
echo "1. Review and update .env file with your configuration"
echo "2. Start the backend: cd backend && cargo run --bin fineract-api"
echo "3. Visit http://localhost:8080 to see the API"
echo "4. Check the health endpoint: curl http://localhost:8080/health"
echo ""
echo "📚 Documentation:"
echo "- README.md - Project overview"
echo "- MIGRATION_PLAN.md - Detailed migration progress"
echo "- DECISIONS.md - Architectural decisions"
echo ""
echo "🛠️ Development Commands:"
echo "- Backend: cd backend && cargo watch -x 'run --bin fineract-api'"
echo "- Tests: cd backend && cargo test"
echo "- Format: cd backend && cargo fmt"
echo "- Lint: cd backend && cargo clippy"