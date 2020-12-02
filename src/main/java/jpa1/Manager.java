package jpa1;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import java.util.List;
import java.util.Scanner;

public class Manager {
    private static EntityManager entityManager;
    private static Scanner scanner;

    public static void main(String[] args) {
        scanner = new Scanner(System.in);
        try {
            entityManager = Persistence.createEntityManagerFactory("JPATest").createEntityManager();
            try {
                while (true) {
                    System.out.println("1: add dish");
                    System.out.println("2: get dish");
                    System.out.println("3: update dish");
                    System.out.println("4: delete dish");
                    System.out.println("5: from reach to expensive dishes");
                    System.out.println("6: dishes with discount");
                    System.out.println("7: order per 1 kg");
                    System.out.println("8: get all dishes");
                    System.out.println("(place for your advertising)-> ");
                    String choice = scanner.nextLine();
                    List<DishEntity> dishEntityList = null;
                    DishEntity dishEntity = new DishEntity();
                    switch (choice) {
                        case "1":
                            addDish(null);
                            break;
                        case "2":
                            getDish(0);
                            break;
                        case "3":
                            dishEntity = new DishEntity("patato free", 26.50, 0.150, 25);
                            dishEntity.setId(1);
                            updateDish(dishEntity);
                            break;
                        case "4":
                            deleteDish(0);
                            break;
                        case "5":
                            dishEntityList = getAllReachToExpensive();
                            for (DishEntity d: dishEntityList) {
                                System.out.println(d);
                            }
                            break;
                        case "6":
                            dishEntityList = withDiscountDishes(0);
                            for (DishEntity d: dishEntityList) {
                                System.out.println(d);
                            }
                            break;
                        case "7":
                            dishEntityList = orderPerAkg();
                            for (DishEntity d: dishEntityList) {
                                System.out.println(d);
                            }
                            break;
                        case "8":
                            dishEntityList = getAll();
                            for (DishEntity d: dishEntityList) {
                                System.out.println(d);
                            }
                            break;
                        default:
                            return;
                    }
                }
            } finally {
                scanner.close();
                entityManager.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static List<DishEntity> orderPerAkg() {
        List<DishEntity> list = null;
        double weight = 0;
        String s = "";
        try {
            while (weight < 1) {
                System.out.print(s);
                System.out.println("What do you want to order? Enter id of dish (for end enter 0):");
                long l = Long.parseLong(scanner.nextLine());
                if (l == 0) return list;
                DishEntity dishEntity = getDish(l);
                weight += dishEntity.getWeight();
                if (weight < 1) {
                    list.add(dishEntity);
                    s = "Current weight of order is " + weight + ", it isn't enough. Must have near a kilogram. \n";
                } else {
                    if (weight == 1) {
                        return list;
                    }else {
                        System.out.println("Current weight of order is " + weight + ", it is too mach. So, change something other");
                        weight -= dishEntity.getWeight();
                        continue;
                    }
                }
            }
        }catch (NumberFormatException nfe) {
            nfe.printStackTrace();
        }
        return list;
    }

    private static List<DishEntity> withDiscountDishes(double discount) {
        return entityManager.createQuery(
                        ("SELECT d FROM DishEntity d WHERE d.discount = "+discount), DishEntity.class)
                        .getResultList();
    }

    private static List<DishEntity> getAllReachToExpensive() {
        String hql = "SELECT d FROM DishEntity d ORDER BY d.id ASC";
        List<DishEntity> list = entityManager.createQuery(hql, DishEntity.class).getResultList();
        return list;
    }

    private static List<DishEntity> getAll() {
        String hql = "SELECT d FROM DishEntity d";
        List<DishEntity> list = entityManager.createQuery(hql, DishEntity.class).getResultList();
        return list;
    }

    private static void deleteDish(long id) {
        if (id == 0) {
            System.out.println("Enter id of dish that you want to delete:");
            try {
                id = Long.parseLong(scanner.nextLine());
            } catch (NumberFormatException nfe) {
                nfe.printStackTrace();
            }
        }
        entityManager.remove(entityManager.find(DishEntity.class, id));
    }

    private static void updateDish(DishEntity dishEntity) {
        entityManager.getTransaction().begin();
        try {
            entityManager.merge(dishEntity);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            e.printStackTrace();
        }
    }

    private static DishEntity getDish(long id) {
        if (id == 0) {
            System.out.println("Enter id of dish:");
            try {
                id = Long.parseLong(scanner.nextLine());
            } catch (NumberFormatException nfe) {
                nfe.printStackTrace();
            }
        }
        DishEntity dishEntity = entityManager.find(DishEntity.class, id);
        System.out.println(dishEntity);
        return dishEntity;
    }

    private static long addDish(DishEntity dishEntity) {
        if (dishEntity == null) {
            dishEntity = new DishEntity();
            System.out.println("What's name of dish:");
            dishEntity.setDishName(scanner.nextLine());
            System.out.println("How mach it cost:");
            try {
                dishEntity.setCost(Double.parseDouble(scanner.nextLine()));
                System.out.println("How mach it weight:");
                dishEntity.setWeight(Double.parseDouble(scanner.nextLine()));
                System.out.println("What's percent of discount:");
                dishEntity.setDiscount(Double.parseDouble(scanner.nextLine()));
            } catch (NumberFormatException nfe) {
                nfe.printStackTrace();
            }
        }
        entityManager.getTransaction().begin();
        try {
            entityManager.persist(dishEntity);
            entityManager.getTransaction().commit();
            System.out.println(dishEntity.getId());
            return dishEntity.getId();
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
        }
        return 0;
    }
}

