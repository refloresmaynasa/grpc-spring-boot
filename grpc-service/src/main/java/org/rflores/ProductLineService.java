package org.rflores;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.rflores.grpc.*;
import org.rflores.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@GrpcService
public class ProductLineService extends ProductLineServiceGrpc.ProductLineServiceImplBase {
    @Autowired
    ProductRepository productRepository;

    public static List<ProductLine> getProductLines() {
        return new ArrayList<>() {
            {
                add(ProductLine.newBuilder().setId("1").setCode("COD1").setTextDescription("Description 1").setHtmlDescription("<b>Hello<b/>").build());
                add(ProductLine.newBuilder().setId("2").setCode("COD2").setTextDescription("Description 2").setHtmlDescription("<b>Hello<b/>").build());
                add(ProductLine.newBuilder().setId("3").setCode("COD3").setTextDescription("Description 3").setHtmlDescription("<b>Hello<b/>").build());
                add(ProductLine.newBuilder().setId("4").setCode("COD4").setTextDescription("Description 4").setHtmlDescription("<b>Hello<b/>").build());
            }
        };
    }

    @Override
    public void getProductLine(ProductLine request, StreamObserver<ProductLine> responseObserver) {
        getProductLines().stream()
                .filter(pl -> pl.getId().equals(request.getId()))
                .findFirst()
                .ifPresent(responseObserver::onNext);

        responseObserver.onCompleted();
    }

    @Override
    public void getProducts(GetAllProductsRequest request, StreamObserver<Product> responseObserver) {
        productRepository.findAll().stream()
                .map(p -> Product.newBuilder()
                        .setId(p.getId())
                        .setColor(p.getColor())
                        .setDescription(p.getDescription())
                        .setGas(p.getGas())
                        .setName(p.getName())
                        .setPrice(p.getPrice())
                        .setYear(p.getYear())
                        .build())
                .forEach(responseObserver::onNext);

        responseObserver.onCompleted();
    }

    @Override
    public StreamObserver<Product> getExpensiveProduct(StreamObserver<Product> responseObserver) {
        return new StreamObserver<>() {
            Product expensiveProduct = null;
            long maxPrice = 0;

            @Override
            public void onNext(Product product) {
                if (product.getPrice() > maxPrice) {
                    maxPrice = product.getPrice();
                    expensiveProduct = product;
                }
            }

            @Override
            public void onError(Throwable throwable) {
                responseObserver.onError(throwable);
            }

            @Override
            public void onCompleted() {
                responseObserver.onNext(expensiveProduct);
                responseObserver.onCompleted();
            }
        };
    }

    @Override
    public StreamObserver<GetProductByYearRequest> getProductByYear(StreamObserver<Product> responseObserver) {
        return new StreamObserver<>() {
            final List<Product> products = new ArrayList<>();

            @Override
            public void onNext(GetProductByYearRequest request) {
                productRepository.findByYear(request.getYear()).stream()
                        .map(p -> Product.newBuilder()
                                .setId(p.getId())
                                .setColor(p.getColor())
                                .setDescription(p.getDescription())
                                .setGas(p.getGas())
                                .setName(p.getName())
                                .setPrice(p.getPrice())
                                .setYear(p.getYear())
                                .build())
                        .forEach(products::add);
            }

            @Override
            public void onError(Throwable throwable) {
                responseObserver.onError(throwable);
            }

            @Override
            public void onCompleted() {
                products.forEach(responseObserver::onNext);
                responseObserver.onCompleted();
            }
        };
    }
}
