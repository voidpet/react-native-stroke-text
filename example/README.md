# StrokeText Test Harness

Expo 54 test app for `@voidpet/react-native-stroke-text`

## Setup

```bash
npm install
```

## Run

Since this library uses native modules, you need to build the native code:

```bash
# iOS
npx expo run:ios

# Android
npx expo run:android
```

## Development Server

After building once, you can use:

```bash
npm start
```

## Features Tested

- Basic stroke text
- Various font sizes
- Different stroke widths
- Color combinations
- Text alignment (left, center, right)
- Multiline text
- Ellipsis truncation
- Number of lines

## Notes

- Library is linked from parent directory via `file:..`
- Rebuild parent library with `cd .. && yarn build` if you make changes
- Native modules require building with `expo run:ios` or `expo run:android`
