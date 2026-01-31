# frozen_string_literal: true

require_relative "noie_nds_api/version"

# Load all generated protobuf descriptors.
require_relative "nds/common/common_pb"
require_relative "nds/common/v1/common_v1_pb"
require_relative "nds/identity/identity_pb"
require_relative "nds/identity/v1/identity_v1_pb"
require_relative "nds/asset/asset_pb"
require_relative "nds/context/context_pb"
require_relative "nds/event/event_pb"
require_relative "nds/event/v1/event_v1_pb"
require_relative "nds/transaction/transaction_pb"
require_relative "nds/query/query_pb"
require_relative "nds/projection/projection_pb"
require_relative "nds/policy/policy_pb"
require_relative "nds/audit/rationale_pb"
require_relative "nds/sync/v1/sync_pb"
require_relative "nds/ledger/v1/ledger_pb"
require_relative "nds/metadata/v1/metadata_pb"
require_relative "nds/memory/v1/memory_pb"

