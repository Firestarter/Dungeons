local dungeonKeys = redis.call("KEYS", "dungeon:*")
local allInfo = {}

for _, dungeonKey in pairs(dungeonKeys) do
    local dungeonData = redis.call("HGETALL", dungeonKey)
    table.insert(allInfo, dungeonData)
end

return allInfo