import React from "react";
import NativeStrokeText, { NativeProps } from "./StrokeTextNativeComponent";

export type StrokeTextProps = NativeProps;

export const StrokeText = (props: StrokeTextProps) => {
  return <NativeStrokeText {...props} />;
};