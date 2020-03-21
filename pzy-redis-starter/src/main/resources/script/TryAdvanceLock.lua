local key = KEYS[1]
local client_id = tostring(ARGV[1])
local expire_time = ARGV[2]
local operate_flag = ARGV[3]
local old_val = redis.call("get",key)

local function add_lock(key, client_id, expire_time)
    redis.call('set', key, client_id)
    redis.call('expire', key, expire_time)
    return true
end

if old_val
then
    -- key存在,判断value是否以operate_flag开头
    if string.find(old_val,string.sub(operate_flag,1,string.len(operate_flag)-1))==1
    then
        -- key存在,且以operate_flag开头, 重新加锁
        return add_lock(key,client_id,expire_time)
    else
        -- key存在,但不以operate_flag开头
        return false
    end
else
    -- key不存在, 则直接加锁, 然后返回true
    return add_lock(key,client_id,expire_time)
end