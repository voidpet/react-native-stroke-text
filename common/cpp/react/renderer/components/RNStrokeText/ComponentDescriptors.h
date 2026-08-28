#pragma once

#include <react/renderer/components/RNStrokeText/StrokeTextShadowNode.h>
#include <react/renderer/core/ConcreteComponentDescriptor.h>
#include <react/renderer/textlayoutmanager/TextLayoutManager.h>
#include <react/utils/ContextContainer.h>

namespace facebook::react {

class VoidpetStrokeTextComponentDescriptor final
    : public ConcreteComponentDescriptor<StrokeTextShadowNode> {
 public:
  explicit VoidpetStrokeTextComponentDescriptor(
      const ComponentDescriptorParameters& parameters)
      : ConcreteComponentDescriptor<StrokeTextShadowNode>(parameters),
        // Same context-container key core Text uses (TextLayoutManagerKey),
        // inlined as a literal: core declares that symbol with a different
        // type across RN versions (extern char[] in 0.81, constexpr char*
        // in 0.86), so referencing it collides in the autolinking TU.
        textLayoutManager_(getManagerByName<TextLayoutManager>(
            contextContainer_,
            "TextLayoutManager")) {}

 protected:
  void adopt(ShadowNode& shadowNode) const override {
    ConcreteComponentDescriptor::adopt(shadowNode);

    auto& strokeTextShadowNode =
        static_cast<StrokeTextShadowNode&>(shadowNode);
    strokeTextShadowNode.setTextLayoutManager(textLayoutManager_);
  }

 private:
  const std::shared_ptr<const TextLayoutManager> textLayoutManager_;
};

} // namespace facebook::react
