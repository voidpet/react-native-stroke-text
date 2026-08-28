#pragma once

#include <ReactCommon/JavaTurboModule.h>
#include <ReactCommon/TurboModule.h>
#include <jsi/jsi.h>
#include <react/renderer/components/RNStrokeText/ComponentDescriptors.h>

namespace facebook::react {

JSI_EXPORT
std::shared_ptr<TurboModule> RNStrokeText_ModuleProvider(
    const std::string& moduleName,
    const JavaTurboModule::InitParams& params) {
  return nullptr;
}

} // namespace facebook::react
