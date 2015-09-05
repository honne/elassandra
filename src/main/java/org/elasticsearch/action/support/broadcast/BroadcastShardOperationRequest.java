/*
 * Licensed to Elasticsearch under one or more contributor
 * license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright
 * ownership. Elasticsearch licenses this file to you under
 * the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.elasticsearch.action.support.broadcast;

import org.elasticsearch.action.IndicesRequest;
import org.elasticsearch.action.OriginalIndices;
import org.elasticsearch.action.support.IndicesOptions;
import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.common.io.stream.StreamOutput;
import org.elasticsearch.index.shard.ShardId;
import org.elasticsearch.transport.TransportRequest;

import java.io.IOException;

/**
 *
 */
public abstract class BroadcastShardOperationRequest extends TransportRequest implements IndicesRequest {

    private ShardId shardId;

    protected OriginalIndices originalIndices;

    protected BroadcastShardOperationRequest() {
    }

    protected BroadcastShardOperationRequest(ShardId shardId, BroadcastOperationRequest request) {
        super(request);
        // this.shardId = shardId;
        this.shardId = new ShardId(shardId.index(), 0); // In elassandra,
                                                        // each node effectively
                                                        // hosts shard at index
                                                        // 0.
        this.originalIndices = new OriginalIndices(request);
    }

    protected BroadcastShardOperationRequest(ShardId shardId, OriginalIndices originalIndices) {
        // this.shardId = shardId;
        this.shardId = new ShardId(shardId.index(), 0); // In elassandra,
                                                        // each node effectively
                                                        // hosts shard at index
                                                        // 0.
        this.originalIndices = originalIndices;
    }

    public ShardId shardId() {
        return this.shardId;
    }

    @Override
    public String[] indices() {
        return originalIndices.indices();
    }

    @Override
    public IndicesOptions indicesOptions() {
        return originalIndices.indicesOptions();
    }

    @Override
    public void readFrom(StreamInput in) throws IOException {
        super.readFrom(in);
        shardId = ShardId.readShardId(in);
        originalIndices = OriginalIndices.readOriginalIndices(in);
    }

    @Override
    public void writeTo(StreamOutput out) throws IOException {
        super.writeTo(out);
        shardId.writeTo(out);
        OriginalIndices.writeOriginalIndices(originalIndices, out);
    }
}
