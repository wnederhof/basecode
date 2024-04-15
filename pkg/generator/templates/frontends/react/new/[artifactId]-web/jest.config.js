const nextJest = require('next/jest')

const createJestConfig = nextJest({
  dir: './',
})

const customJestConfig = {
  setupFilesAfterEnv: ['<rootDir>/jest.setup.js'],
  moduleDirectories: ['node_modules', '<rootDir>/'],
  testEnvironment: 'jest-environment-jsdom',
  moduleNameMapper: {
    "^\\@lib/(.*)$": "<rootDir>/src/lib/$1",
    "^\\@components/(.*)$": "<rootDir>/src/components/$1",
    "^\\@generated/(.*)$": "<rootDir>/src/generated/$1"
  }
}

module.exports = createJestConfig(customJestConfig)
