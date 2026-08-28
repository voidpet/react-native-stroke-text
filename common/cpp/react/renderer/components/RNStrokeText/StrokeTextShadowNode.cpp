#include "StrokeTextShadowNode.h"

#include <algorithm>
#include <cmath>

#include <react/renderer/attributedstring/AttributedString.h>
#include <react/renderer/attributedstring/AttributedStringBox.h>
#include <react/renderer/attributedstring/ParagraphAttributes.h>
#include <react/renderer/attributedstring/TextAttributes.h>
#include <react/renderer/textlayoutmanager/TextLayoutContext.h>
#include <react/debug/react_native_assert.h>

namespace facebook::react {

void StrokeTextShadowNode::setTextLayoutManager(
    std::shared_ptr<const TextLayoutManager> textLayoutManager) {
  ensureUnsealed();
  textLayoutManager_ = std::move(textLayoutManager);
}

Size StrokeTextShadowNode::measureContent(
    const LayoutContext& layoutContext,
    const LayoutConstraints& layoutConstraints) const {
  react_native_assert(textLayoutManager_);

  const auto& props = getConcreteProps();

  auto textAttributes = TextAttributes::defaultTextAttributes();
  textAttributes.fontSize = props.fontSize;
  textAttributes.fontFamily = props.fontFamily;
  // This component is density-scaled only; OS font scale is deliberately
  // ignored so drawing and measurement agree (see StrokedTextView DIP units).
  textAttributes.fontSizeMultiplier = 1.0;
  textAttributes.allowFontScaling = false;

  auto fragment = AttributedString::Fragment{};
  fragment.string = props.text;
  fragment.textAttributes = textAttributes;

  auto attributedString = AttributedString{};
  attributedString.appendFragment(std::move(fragment));
  attributedString.setBaseTextAttributes(textAttributes);

  auto paragraphAttributes = ParagraphAttributes{};
  paragraphAttributes.maximumNumberOfLines = std::max(0, props.numberOfLines);
  paragraphAttributes.includeFontPadding = true;
  if (props.ellipsis) {
    paragraphAttributes.ellipsizeMode = EllipsizeMode::Tail;
  }

  const auto pointScale = layoutContext.pointScaleFactor;
  const auto insetDp =
      std::ceil(std::max<Float>(0, props.strokeWidth) * pointScale) /
      pointScale;
  // Android pads ceil(strokeWidthPx) on each side, which is exactly insetDp.
  const auto totalStrokeInset = 2 * insetDp;

  auto textConstraints = layoutConstraints;
  textConstraints.minimumSize = Size{0, 0};
  textConstraints.maximumSize = Size{
      std::max<Float>(0, layoutConstraints.maximumSize.width - totalStrokeInset),
      std::max<Float>(0, layoutConstraints.maximumSize.height - totalStrokeInset),
  };

  const auto textLayoutContext = TextLayoutContext{
      .pointScaleFactor = layoutContext.pointScaleFactor,
      .surfaceId = getSurfaceId(),
  };

  auto measuredSize = textLayoutManager_
                          ->measure(
                              AttributedStringBox{attributedString},
                              paragraphAttributes,
                              textLayoutContext,
                              textConstraints)
                          .size;

  measuredSize.width += totalStrokeInset;
  measuredSize.height += totalStrokeInset;
  return layoutConstraints.clamp(measuredSize);
}

} // namespace facebook::react
