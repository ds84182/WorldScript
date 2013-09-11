--- The WorldObject interfaces with the Minecraft World.
WorldObject = {}

--- Gets the id of the block at the specified coordinates
-- @param x The x coordinate
-- @param y The y coordinate
-- @param z The z coordinate
-- @return The ID of the block, or zero if it cannot be found (or air).
function WorldObject.getBlockID(x,y,z) end

--- Sets the id of the block at the specified coordinates
-- @param x The x coordinate
-- @param y The y coordinate
-- @param z The z coordinate
-- @param id The block id
-- @param meta (Optional) The block metadata value
function WorldObject.setBlockID(x,y,z,id,meta) end
