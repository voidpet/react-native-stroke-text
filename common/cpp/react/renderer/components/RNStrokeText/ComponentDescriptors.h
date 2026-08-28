#pragma once

#include <react/renderer/components/RNStrokeText/StrokeTextShadowNode.h>
#include <react/renderer/core/ConcreteComponentDescriptor.h>
#include <react/renderer/textlayoutmanager/TextLayoutManager.h>
#include <react/utils/ContextContainer.h>

namespace facebook::react {

extern const char TextLayoutManagerKey[];

class VoidpetStrokeTextComponentDescriptor final
    : public ConcreteComponentDescriptor<StrokeTextShadowNode> {
 public:
  explicit VoidpetStrokeTextComponentDescriptor(
      const ComponentDescriptorParameters& parameters)
      : ConcreteComponentDescriptor<StrokeTextShadowNode>(parameters),
        textLayoutManager_(getManagerByName<TextLayoutManager>(
            contextContainer_,
            TextLayoutManagerKey)) {}

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
