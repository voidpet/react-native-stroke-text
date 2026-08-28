import { useEffect, useState } from "react";
import { useFonts } from "expo-font";
import { StatusBar } from "expo-status-bar";
import { Button, ScrollView, StyleSheet, Text, View } from "react-native";
import { StrokeText } from "@voidpet/react-native-stroke-text";

const COLORS = {
  fill: "#FFE66D",
  stroke: "#25213C",
};

export default function App() {
  const [loaded] = useFonts({
    "Inter-Bold": require("./assets/Inter-Bold.ttf"),
  });
  const [count, setCount] = useState(1);
  const [mountKey, setMountKey] = useState(0);

  useEffect(() => {
    const interval = setInterval(() => {
      setCount((value) => (value >= 60 ? 1 : value + 1));
    }, 500);
    return () => clearInterval(interval);
  }, []);

  if (!loaded) {
    return null;
  }

  const alternatingText = count % 2 === 0 ? "999 ATK" : "1";

  return (
    <ScrollView style={styles.screen} contentContainerStyle={styles.content}>
      <StatusBar style="light" />
      <Text style={styles.title}>Fabric measurement lab</Text>
      <Text style={styles.caption}>Counter and alternating samples update in place without key remounts.</Text>

      <View key={mountKey} style={styles.samples}>
        <Section title="In-place counter 1–60">
          <StrokeText
            text={String(count)}
            fontSize={52}
            color={COLORS.fill}
            strokeColor={COLORS.stroke}
            strokeWidth={5}
            fontFamily="Inter-Bold"
          />
        </Section>

        <Section title="Short ↔ long intrinsic resize">
          <View style={styles.alternatingRow}>
            <StrokeText
              text={alternatingText}
              fontSize={34}
              color="#A9F0D1"
              strokeColor="#173A33"
              strokeWidth={3}
              fontFamily="Inter-Bold"
            />
            <Text style={styles.marker}>← frame should hug text</Text>
          </View>
        </Section>

        <Section title="Stroke inset: 0 / 2 / 6">
          <View style={styles.row}>
            {[0, 2, 6].map((strokeWidth) => (
              <StrokeText
                key={strokeWidth}
                text={`S${strokeWidth}`}
                fontSize={30}
                color="#FFFFFF"
                strokeColor="#EA4C89"
                strokeWidth={strokeWidth}
                fontFamily="Inter-Bold"
              />
            ))}
          </View>
        </Section>

        <Section title="Alignment in fixed frames">
          {(["left", "center", "right"] as const).map((align) => (
            <View key={align} style={styles.alignSample}>
              <StrokeText
                text={align.toUpperCase()}
                width={220}
                fontSize={24}
                color="#B5C8FF"
                strokeColor="#253563"
                strokeWidth={2}
                align={align}
                fontFamily="Inter-Bold"
              />
            </View>
          ))}
        </Section>

        <Section title="Ellipsis + explicit width">
          <View style={styles.alignSample}>
            <StrokeText
              text="THIS LABEL MUST ELLIPSIZE CLEANLY"
              width={220}
              fontSize={24}
              color="#FFC9DE"
              strokeColor="#5A1731"
              strokeWidth={2}
              numberOfLines={1}
              ellipsis
              fontFamily="Inter-Bold"
            />
          </View>
        </Section>

        <Section title="Multiline + default font">
          <StrokeText
            text={"MULTILINE\nSTAYS TIGHT"}
            fontSize={27}
            color="#D7F7A8"
            strokeColor="#304A16"
            strokeWidth={3}
          />
        </Section>
      </View>

      <View style={styles.button}>
        <Button title="Remount all samples" onPress={() => setMountKey((value) => value + 1)} />
      </View>
    </ScrollView>
  );
}

function Section({ children, title }: { children: React.ReactNode; title: string }) {
  return (
    <View style={styles.section}>
      <Text style={styles.sectionTitle}>{title}</Text>
      {children}
    </View>
  );
}

const styles = StyleSheet.create({
  screen: {
    flex: 1,
    backgroundColor: "#11101A",
  },
  content: {
    paddingHorizontal: 20,
    paddingTop: 64,
    paddingBottom: 48,
  },
  title: {
    color: "#FFFFFF",
    fontSize: 26,
    fontWeight: "700",
  },
  caption: {
    color: "#AAA7BC",
    fontSize: 14,
    marginBottom: 24,
    marginTop: 6,
  },
  samples: {
    gap: 14,
  },
  section: {
    alignItems: "flex-start",
    backgroundColor: "#1D1B2A",
    borderColor: "#353146",
    borderRadius: 12,
    borderWidth: 1,
    padding: 14,
  },
  sectionTitle: {
    color: "#AAA7BC",
    fontSize: 12,
    fontWeight: "600",
    letterSpacing: 0.5,
    marginBottom: 10,
    textTransform: "uppercase",
  },
  alternatingRow: {
    alignItems: "center",
    flexDirection: "row",
  },
  marker: {
    color: "#777389",
    fontSize: 12,
    marginLeft: 8,
  },
  row: {
    alignItems: "center",
    flexDirection: "row",
    gap: 18,
  },
  alignSample: {
    backgroundColor: "#12111B",
    borderColor: "#413C54",
    borderWidth: 1,
    marginBottom: 7,
    width: 220,
  },
  button: {
    marginTop: 22,
  },
});
