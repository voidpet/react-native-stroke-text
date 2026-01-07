import type { HostComponent } from "react-native";
import codegenNativeComponent from "react-native/Libraries/Utilities/codegenNativeComponent";
import { WithDefault, Float, Int32 } from "react-native/Libraries/Types/CodegenTypes";
import type { ViewProps } from "react-native/Libraries/Components/View/ViewPropTypes";

type TextAlign = "center" | "left" | "right";

export interface NativeProps extends ViewProps {
  text: string;
  fontSize?: WithDefault<Float, 14>;
  color?: string;
  strokeColor?: string;
  strokeWidth?: WithDefault<Float, 0>;
  fontFamily?: string;
  align?: WithDefault<TextAlign, "left">;
  numberOfLines?: WithDefault<Int32, 0>;
  ellipsis?: boolean;
  width?: WithDefault<Float, 0>;
}

export default codegenNativeComponent<NativeProps>("StrokeTextView") as HostComponent<NativeProps>;
