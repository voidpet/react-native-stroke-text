/**
 * @type {import('@react-native-community/cli-types').UserDependencyConfig}
 */
module.exports = {
  dependency: {
    platforms: {
      android: {
        libraryName: "RNStrokeText",
        componentDescriptors: ["VoidpetStrokeTextComponentDescriptor"],
        cmakeListsPath: "src/main/jni/CMakeLists.txt",
      },
      ios: {},
    },
  },
};
