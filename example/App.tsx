import { StatusBar } from "expo-status-bar";
import { StyleSheet, View, Text } from "react-native";
import { StrokeText } from "@voidpet/react-native-stroke-text";
import { useFonts } from "expo-font";

export default function App() {
  const [loaded] = useFonts({
    "Inter-Bold": require("./assets/Inter-Bold.ttf"),
    "Bangers-Regular": require("./assets/Bangers-Regular.ttf"),
  });

  if (!loaded) {
    return null;
  }

  return (
    <View style={styles.container}>
      <StrokeText
        text="Hello World"
        fontSize={30}
        color="#000000"
        strokeColor="#FF0000"
        strokeWidth={5}
        fontFamily="Inter-Bold"
      />
      <StatusBar style="auto" />
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: "#fff",
    alignItems: "center",
    justifyContent: "center",
  },
});
