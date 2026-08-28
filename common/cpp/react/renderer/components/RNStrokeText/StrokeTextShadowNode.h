#pragma once

#include <react/renderer/components/RNStrokeTextSpec/ShadowNodes.h>
#include <react/renderer/components/view/ConcreteViewShadowNode.h>
#include <react/renderer/core/LayoutConstraints.h>
#include <react/renderer/core/LayoutContext.h>
#include <react/renderer/textlayoutmanager/TextLayoutManager.h>

namespace facebook::react {

class StrokeTextShadowNode final
    : public ConcreteViewShadowNode<
          StrokeTextViewComponentName,
          StrokeTextViewProps,
          StrokeTextViewEventEmitter> {
 public:
  using ConcreteViewShadowNode::ConcreteViewShadowNode;

  static ShadowNodeTraits BaseTraits() {
    auto traits = ConcreteViewShadowNode::BaseTraits();
    traits.set(ShadowNodeTraits::Trait::LeafYogaNode);
    traits.set(ShadowNodeTraits::Trait::MeasurableYogaNode);
    return traits;
  }

  void setTextLayoutManager(
      std::shared_ptr<const TextLayoutManager> textLayoutManager);

  Size measureContent(
      const LayoutContext& layoutContext,
      const LayoutConstraints& layoutConstraints) const override;

 private:
  std::shared_ptr<const TextLayoutManager> textLayoutManager_;
};

} // namespace facebook::react
