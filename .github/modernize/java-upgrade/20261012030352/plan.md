# Upgrade Plan: UserLog (20261012030352)

- **Generated**: 2026-10-12 03:03:52
- **HEAD Branch**: N/A
- **HEAD Commit ID**: N/A

## Available Tools

**JDKs**
- JDK 9: not available (baseline will be skipped)
- JDK 25: **<TO_BE_INSTALLED>** (required by upgrade and final validation)

**Build Tools**
- Maven 3.9.12: C:\Program Files\Apache\Maven\apache-maven-3.9.12\bin
- Maven Wrapper: 3.8.5 → **<TO_BE_UPGRADED>** to 3.9.12

## Guidelines

> Note: You can add any specific guidelines or constraints for the upgrade process here if needed, bullet points are preferred.

## Options

- Working branch: appmod/java-upgrade-20261012030352
- Run tests before and after the upgrade: true

## Upgrade Goals

- Java runtime/compiler target: 25 (latest LTS)

## Technology Stack

| Technology/Dependency | Current | Min Compatible Version | Why Incompatible |
| --------------------- | ------- | ---------------------- | ---------------- |
| Java compiler target | 9 | 25 | User requested Java 25 |
| Maven Wrapper | 3.8.5 | 3.9.12 | Upgrade to the installed supported Maven 3.9 line for Java 25 compatibility |
| maven-compiler-plugin | 3.13.0 | 3.14.1 | Use a stable compiler plugin version with Java 25 support |
| JavaFX controls/FXML/web/swing | 17.0.6 | 17.0.6 | No compile-time Java 25 incompatibility identified; retain existing dependency line |
| JUnit Jupiter | 5.10.2 | 5.10.2 | No Java 25 incompatibility identified |

## Derived Upgrades

- Upgrade Maven Wrapper from 3.8.5 to 3.9.12 to align the wrapper with the installed Maven 3.9.12 and use a current Maven 3.9 release with Java 25.
- Upgrade maven-compiler-plugin from 3.13.0 to 3.14.1 and configure `maven.compiler.release` to 25 so compilation targets Java 25 APIs and bytecode.

## Impact Analysis

### Dependency Changes

| File | Dependency | Current | Action | Target | Reason |
|------|-----------|---------|--------|--------|--------|
| pom.xml | maven-compiler-plugin | 3.13.0 | upgrade | 3.14.1 | Stable compiler plugin version supporting Java 25 |
| pom.xml | compiler source/target | 9 | replace | release 25 | Align produced bytecode and exposed JDK APIs with requested runtime |
| .mvn/wrapper/maven-wrapper.properties | Maven distribution | 3.8.5 | upgrade | 3.9.12 | Match the available Maven installation and use Maven 3.9 with Java 25 |

### Source Code Changes

No Java source changes are currently indicated. The module descriptor declares standard JavaFX and application modules; compilation at release 25 will verify those module references.

### Configuration Changes

No application configuration changes identified.

### CI/CD Changes

No CI/CD files were present in the inspected workspace structure.

### Risks & Warnings

- **JavaFX runtime validation**: The project uses JavaFX 17.0.6, and a successful compile/test does not exercise native JavaFX startup on Java 25. **Mitigation**: Retain the existing JavaFX line for this runtime-target change, complete clean compilation and tests, and record GUI startup as a residual runtime check if this environment cannot launch JavaFX.
- **No Git metadata**: The workspace is not a Git repository. Changes cannot be put on a branch or committed by this workflow; files will remain in the workspace.
- **No tests discovered**: No test source directory was included in the workspace structure. Run Maven's test lifecycle and record the resulting test count.

### CVE Validation & Fix

After the Java target change, extract direct Maven dependencies and run the Java dependency CVE scan. Upgrade any direct dependency or BOM-managed version for which the scan reports an available fix, then recompile and rescan.

## Upgrade Steps

- Step 1: Setup Environment
  - **Rationale**: Java 25 is not installed and is required for the target build and runtime verification.
  - **Changes to Make**: Install JDK 25. Maven 3.9.12 is already installed; the wrapper update is included with the project build configuration step.
  - **Verification**: `appmod-list-jdks`, JDK 25; expected result: JDK 25 is available.

- Step 2: Setup Baseline
  - **Rationale**: Capture the pre-upgrade build and test state when the project's current JDK is available.
  - **Changes to Make**: None.
  - **Verification**: Base JDK 9 is not installed; skip baseline compilation and tests as required by the workflow.

- Step 3: Configure Java 25 and Maven Wrapper
  - **Rationale**: Update the compiler target and build tooling together so the project compiles against Java 25 using the supported wrapper version.
  - **Changes to Make**: Apply all entries in Impact Analysis > Dependency Changes.
  - **Verification**: `mvnw.cmd clean test-compile -q`, JDK 25; expected result: production and test code compile successfully.

- Step 4: CVE Validation & Fix
  - **Rationale**: Verify direct dependencies after the runtime/build configuration update and remediate any reported vulnerabilities with available fixes.
  - **Changes to Make**: Apply only dependency version changes required by scan findings; retain unrelated dependency versions.
  - **Verification**: Run dependency scan, `mvnw.cmd clean test-compile -q` if any dependency is changed, then rescan; expected result: all findings with available fixes are resolved.

- Step 5: Final Validation
  - **Rationale**: Confirm all upgrade goals, compilation, tests, and dependency scan results on the requested runtime.
  - **Changes to Make**: Resolve any build/test failures uncovered by validation; leave no upgrade TODOs or temporary workarounds.
  - **Verification**: With JDK 25, run `mvnw.cmd clean test-compile -q` and `mvnw.cmd clean test -q`; expected result: compilation succeeds and all tests pass. If no tests are present, record Maven's test count.

> Version control is unavailable because this workspace is not a Git repository. Upgrade changes will remain uncommitted in the working directory.
