package kr.sul.statuseffect.item.food

import kr.sul.servercore.util.ItemBuilder.loreIB
import kr.sul.servercore.util.ItemBuilder.nameIB
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

// Enum의 고유 property인 name과 itemName 혼동 주의
enum class FoodType(val material: Material, val itemName: String, val addHealth: Double, val addFoodLevel: Double, val additionalLore: List<String>? = null) {
    APPLE_CANNED(Material.GOLDEN_APPLE, "§c[ §f사과 통조림 §c]", 0.5, 2.0),
    CARROT_CANNED(Material.CARROT_ITEM, "§c[ §f당근 통조림 §c]", 0.5, 2.0),
    SARDINE_CANNED(Material.RAW_FISH, "§c[ §f정어리 통조림 §c]", 1.0, 2.5),
    CHICKEN_CANNED(Material.RAW_CHICKEN,"§c[ §f닭고기 통조림 §c]", 1.5, 3.0),
    RABBIT_CANNED(Material.RABBIT,"§c[ §f토끼고기 통조림 §c]", 1.5, 3.0),
//    POTATO_CHIP(Material.POTATO_ITEM,"포테이토칩", 1.5, 3.0),
    PORKCHOP_CANNED(Material.PORK,"§c[ §f돼지고기 통조림 §c]", 2.0, 4.0),
    MUTTON_CANNED(Material.MUTTON,"§c[ §f양고기 통조림 §c]", 2.0, 4.0);
//    RABBIT(Material.UNKNOWN,"토끼고기", 1.5, 3.0),
//    POTATO(Material.UNKNOWN,"구운 감자", 1.5, 3.0),
//    SALMON(Material.UNKNOWN,"연어", 2.5, 3.0),
//    BEEF_JERKY(Material.UNKNOWN,"소고기 육포", 2.0, 4.0),
//    BREAD(Material.UNKNOWN,"보급용 빵", 1.0, 2.5),
//    SALMON(Material.UNKNOWN,"초코바", 1.0, 2.5);




    // https://pjh3749.tistory.com/279 로 개선 여지 있음
    companion object {
        fun find(material: Material, itemName: String): FoodType? {
            return values().find { it.material == material && it.itemName == itemName }
        }

        // TODO: 하트, 핫도그 완성시키고, 아이템 뽑아낼 수 있는 커맨드 만들기
        fun getItem(food: FoodType): ItemStack {
            food.run {
                val item = ItemStack(material)
                item.nameIB(itemName)
                item.loreIB("§7섭취시 회복량")
                item.loreIB("")  // 하트 모양 addHealth 만큼 넣기
                item.loreIB("§7섭취시 포만감")
                item.loreIB("")  // 핫도그 모양 addFoodLevel 만큼 넣기

                additionalLore?.forEach { lore ->
                    item.loreIB(lore)
                }

                return item
            }
        }
    }
}