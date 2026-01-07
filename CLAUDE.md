# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Package Information

Published as `@voidpet/react-native-stroke-text` on npm. React Native library for rendering text with stroke/outline effects on both iOS and Android.

## Build and Release Commands

```bash
# Build TypeScript to JavaScript
yarn build

# Release new versions (builds, increments version, publishes, pushes to git)
yarn release:patch  # 1.2.3 → 1.2.4
yarn release:minor  # 1.2.3 → 1.3.0
yarn release:major  # 1.2.3 → 2.0.0
```

## Example App Development

The `example/` directory contains an Expo app for testing. Since this is a native module library:

```bash
cd example
npm install

# Must use native builds (not Expo Go)
npx expo run:ios
npx expo run:android

# After native build, can use dev server
npm start
```

After changing native code or TypeScript in the library, rebuild from root with `yarn build`.

## Architecture

This is a **React Native Fabric (new architecture) native component** using Codegen.

### Component Flow
1. **JS Layer** (`src/index.tsx`): Thin wrapper exporting `StrokeText` component
2. **Codegen Spec** (`src/StrokeTextNativeComponent.ts`): Defines native component interface using Codegen types
3. **Native Android** (`android/src/main/java/com/stroketext/`): Custom view rendering stroked text
4. **Native iOS** (`ios/`): Custom UIView with Core Graphics stroke rendering

### Key Files

- `src/StrokeTextNativeComponent.ts` - Codegen spec defining props interface (generates native glue code)
- `android/src/main/java/com/stroketext/StrokedTextView.java` - Android custom view with Canvas stroke drawing
- `android/src/main/java/com/stroketext/StrokeTextViewManager.java` - React Native view manager (uses Codegen delegate)
- `ios/StrokeTextView.swift` - iOS view wrapper coordinating with RCTBridge
- `ios/StrokedTextLabel.swift` - UILabel subclass doing Core Graphics stroke rendering

### Codegen

Codegen generates ViewManager interfaces and delegates from the TypeScript spec. Generated files appear in `android/build/generated/source/codegen/` during Android build. The `codegenConfig` in `package.json` configures this:

```json
"codegenConfig": {
  "name": "RNStrokeTextSpec",
  "type": "components",
  "jsSrcsDir": "src"
}
```

### Platform-Specific Notes

**Android:** Requires `compileSdkVersion` 34. Uses custom Canvas drawing with Paint stroke. Font loading via `FontUtil.java`.

**iOS:** Uses Core Graphics `CGContext` for stroke rendering. Font caching implemented in `StrokeTextView.swift`. Supports hex colors (#RGB/#RRGGBB), rgb(), and rgba() formats.

## Props Reference

All props defined in `src/StrokeTextNativeComponent.ts`:
- `text` (string, required)
- `fontSize` (number, default: 14)
- `color` (string, text fill color)
- `strokeColor` (string, outline color)
- `strokeWidth` (number, outline thickness)
- `fontFamily` (string, must match loaded fonts)
- `align` ("center" | "left" | "right")
- `numberOfLines` (number, 0 = unlimited)
- `ellipsis` (boolean, requires `width` prop)
- `width` (number, for ellipsis truncation)
