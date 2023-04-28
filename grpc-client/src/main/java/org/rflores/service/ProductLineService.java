package org.rflores.service;

import com.google.protobuf.Descriptors;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.rflores.grpc.*;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@Service
public class ProductLineService {

    @GrpcClient("grpc-rflores-service")
    ProductLineServiceGrpc.ProductLineServiceBlockingStub synchronousClient;

    @GrpcClient("grpc-rflores-service")
    ProductLineServiceGrpc.ProductLineServiceStub asynchronousClient;

    public Map<Descriptors.FieldDescriptor, Object> getProductLine(String productLineId) {
        var productLineRequest = ProductLine.newBuilder().setId(productLineId).build();
        var response = synchronousClient.getProductLine(productLineRequest);
        return response.getAllFields();
    }

    public List<Map<Descriptors.FieldDescriptor, Object>> getProducts() throws InterruptedException {
        var request = GetAllProductsRequest.newBuilder().build();
        final var countDownLanch = new CountDownLatch(1);
        final List<Map<Descriptors.FieldDescriptor, Object>> response = new ArrayList<>();

        asynchronousClient.getProducts(request, new StreamObserver<Product>() {
            @Override
            public void onNext(Product product) {
                response.add(product.getAllFields());
            }

            @Override
            public void onError(Throwable t) {
                countDownLanch.countDown();
            }

            @Override
            public void onCompleted() {
                countDownLanch.countDown();
            }
        });

        boolean await = countDownLanch.await(1, TimeUnit.MINUTES);
        return await ? response : Collections.emptyList();
    }

    public Map<String, Map<Descriptors.FieldDescriptor, Object>> getExpensiveProduct() throws InterruptedException {
        var request = GetAllProductsRequest.newBuilder().build();
        final var countDownLanchProduct = new CountDownLatch(1);
        final List<Product> products = new ArrayList<>();

        asynchronousClient.getProducts(request, new StreamObserver<Product>() {
            @Override
            public void onNext(Product product) {
                products.add(product);
            }

            @Override
            public void onError(Throwable t) {
                countDownLanchProduct.countDown();
            }

            @Override
            public void onCompleted() {
                countDownLanchProduct.countDown();
            }
        });
        var awaitProduct = countDownLanchProduct.await(1, TimeUnit.MINUTES);

        final CountDownLatch countDownLatch = new CountDownLatch(1);
        final Map<String, Map<Descriptors.FieldDescriptor, Object>> response = new HashMap<>();

        StreamObserver<Product> responseObserver = asynchronousClient.getExpensiveProduct(new StreamObserver<Product>() {
            @Override
            public void onNext(Product product) {
                response.put("ExpensiveProduct", product.getAllFields());
            }

            @Override
            public void onError(Throwable throwable) {
                countDownLatch.countDown();
            }

            @Override
            public void onCompleted() {
                countDownLatch.countDown();
            }
        });

        products.forEach(responseObserver::onNext);
        responseObserver.onCompleted();
        var await = countDownLatch.await(1, TimeUnit.MINUTES);
        return await ? response : Collections.emptyMap();
    }

    public List<Map<Descriptors.FieldDescriptor, Object>> getProductsByYear(Integer[] years) throws InterruptedException {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        final List<Map<Descriptors.FieldDescriptor, Object>> response = new ArrayList<>();

        var responseObserver = asynchronousClient.getProductByYear(new StreamObserver<Product>() {
            @Override
            public void onNext(Product product) {
                response.add(product.getAllFields());
            }

            @Override
            public void onError(Throwable throwable) {
                countDownLatch.countDown();
            }

            @Override
            public void onCompleted() {
                countDownLatch.countDown();
            }
        });

        Arrays.stream(years).forEach(y -> responseObserver.onNext(GetProductByYearRequest.newBuilder().setYear(y).build()));
        responseObserver.onCompleted();

        var await = countDownLatch.await(1, TimeUnit.MINUTES);
        return await ? response : Collections.emptyList();
    }
}
