# ==========================================
# Stage 1: Build & Pre-cache dependencies
# ==========================================
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# Copy Maven descriptor and pre-fetch dependencies for fast layered caching
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy project files and compile tests
COPY src ./src
COPY webpage ./webpage
COPY testng.xml .
RUN mvn test-compile -B

# ==========================================
# Stage 2: Runtime with Chrome & Maven
# ==========================================
FROM maven:3.9.6-eclipse-temurin-17 AS runtime
WORKDIR /app

# Install Google Chrome stable and its required dependencies for headless runs
RUN apt-get update && apt-get install -y \
    wget \
    gnupg \
    curl \
    unzip \
    libglib2.0-0 \
    libnss3 \
    libgconf-2-4 \
    libfontconfig1 \
    ca-certificates \
    fonts-liberation \
    && rm -rf /var/lib/apt/lists/* \
    && curl -fsSL https://dl.google.com/linux/linux_signing_key.pub | gpg --dearmor -o /usr/share/keyrings/google-linux-signing-keyring.gpg \
    && echo "deb [arch=amd64 signed-by=/usr/share/keyrings/google-linux-signing-keyring.gpg] http://dl.google.com/linux/chrome/deb/ stable main" > /etc/apt/sources.list.d/google-chrome.list \
    && apt-get update && apt-get install -y google-chrome-stable \
    && rm -rf /var/lib/apt/lists/*

# Copy cached dependencies and compiled artifacts from build stage
COPY --from=build /root/.m2 /root/.m2
COPY --from=build /app /app

# Set environment variables for Chrome
ENV CHROME_BIN=/usr/bin/google-chrome-stable
ENV HEADLESS=true

# Default command to run the TestNG test suite inside container
CMD ["mvn", "test"]
