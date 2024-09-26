// config-overrides.js
module.exports = function override(config, env) {
    config.resolve.fallback = {
      crypto: require.resolve('crypto-browserify'),
      vm: require.resolve("vm-browserify"),
      buffer: require.resolve("buffer/"),
      stream: require.resolve("stream-browserify")
    };
    return config;
};
  