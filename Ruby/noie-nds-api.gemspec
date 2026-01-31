# frozen_string_literal: true

require_relative "lib/noie_nds_api/version"

Gem::Specification.new do |spec|
  spec.name = "noie-nds-api"
  spec.version = Noie::Nds::Api::VERSION
  spec.summary = "NDS Protocol Buffers types for Ruby (generated from nds-api/spec)."
  spec.description = "Generated Ruby protobuf types for NoieDigitalSystem (NDS) protocol."
  spec.license = "Apache-2.0"
  spec.required_ruby_version = ">= 4.0.1"

  spec.authors = ["Misty4119"]
  spec.homepage = "https://github.com/Misty4119/nds-api"
  spec.metadata["source_code_uri"] = "https://github.com/Misty4119/nds-api"
  spec.metadata["bug_tracker_uri"] = "https://github.com/Misty4119/nds-api/issues"

  spec.files = Dir.glob("lib/**/*.rb") + ["README.md"]
  spec.require_paths = ["lib"]

  spec.add_dependency "google-protobuf", ">= 4"
end

