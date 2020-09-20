<template>
  <div>
    <Nuxt />
  </div>
</template>

<style>
html {
  font-family:
    'Source Sans Pro',
    -apple-system,
    BlinkMacSystemFont,
    'Segoe UI',
    Roboto,
    'Helvetica Neue',
    Arial,
    sans-serif;
  font-size: 16px;
  word-spacing: 1px;
  -ms-text-size-adjust: 100%;
  -webkit-text-size-adjust: 100%;
  -moz-osx-font-smoothing: grayscale;
  -webkit-font-smoothing: antialiased;
  box-sizing: border-box;
}

/*
 * This transition delays the page transition by 100ms while the components are in fact being rendered.
 * This way, fast components that need data from the server are already fully rendered
 * by the time the page is loaded.
 */
.page-enter-active,
.page-leave-active {
  transition-duration: .1s;
  transition-property: none;
}

.page-leave,
.page-enter-active,
.page-enter,
.page-leave-active {
  display: none;
}

</style>
